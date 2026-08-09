package views;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import javax.swing.SwingUtilities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Tests that a request really does leave the event dispatch thread and really does come back.
 *
 * <p>Both halves matter. Work left on the event dispatch thread freezes the window; a callback
 * that arrives anywhere else touches Swing from the wrong thread, which fails unpredictably
 * rather than outright.
 */
class BackgroundRequestTest {
    private static final int TIMEOUT_SECONDS = 10;
    private static final int RESPONSIVENESS_TIMEOUT_SECONDS = 5;

    @Test
    void runsWorkOffTheEventDispatchThreadAndReportsBackOnIt() throws Exception {
        final AtomicBoolean workWasOnEventThread = new AtomicBoolean(true);
        final AtomicBoolean callbackWasOnEventThread = new AtomicBoolean(false);
        final AtomicBoolean reported = new AtomicBoolean(false);
        final CountDownLatch finished = new CountDownLatch(1);

        request(
            () -> {
                workWasOnEventThread.set(SwingUtilities.isEventDispatchThread());
            },
            succeeded -> {
                callbackWasOnEventThread.set(SwingUtilities.isEventDispatchThread());
                reported.set(succeeded);
                finished.countDown();
            });

        assertTrue(finished.await(TIMEOUT_SECONDS, TimeUnit.SECONDS));
        assertFalse(workWasOnEventThread.get());
        assertTrue(callbackWasOnEventThread.get());
        assertTrue(reported.get());
    }

    @Test
    void reportsFailureWhenTheWorkThrows() throws Exception {
        final AtomicBoolean reported = new AtomicBoolean(true);
        final CountDownLatch finished = new CountDownLatch(1);

        request(
            () -> {
                throw new IllegalStateException("nothing answered");
            },
            succeeded -> {
                reported.set(succeeded);
                finished.countDown();
            });

        assertTrue(finished.await(TIMEOUT_SECONDS, TimeUnit.SECONDS));
        // The window must be told, rather than the failure vanishing into a background thread.
        assertFalse(reported.get());
    }

    @Test
    @Timeout(RESPONSIVENESS_TIMEOUT_SECONDS)
    void doesNotBlockTheEventDispatchThreadWhileTheWorkRuns() throws Exception {
        final CountDownLatch released = new CountDownLatch(1);
        final CountDownLatch finished = new CountDownLatch(1);

        request(
            () -> {
                awaitQuietly(released);
            },
            succeeded -> {
                finished.countDown();
            });

        // The work is still blocked, yet the event dispatch thread answers straight away. Were the
        // work running there, this test would sit until its timeout expired instead.
        final CountDownLatch responsive = new CountDownLatch(1);
        SwingUtilities.invokeLater(responsive::countDown);
        assertTrue(responsive.await(TIMEOUT_SECONDS, TimeUnit.SECONDS));

        released.countDown();
        assertTrue(finished.await(TIMEOUT_SECONDS, TimeUnit.SECONDS));
    }

    private static void request(Runnable work, Consumer<Boolean> whenFinished) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            BackgroundRequest.run(work, whenFinished);
        });
    }

    private static void awaitQuietly(CountDownLatch latch) {
        try {
            latch.await(TIMEOUT_SECONDS, TimeUnit.SECONDS);
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
}
