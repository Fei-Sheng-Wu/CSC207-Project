package views;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import javax.swing.SwingWorker;

/**
 * Runs a request away from the event dispatch thread.
 *
 * <p>Swing paints and delivers input on one thread, so anything slow invoked from a listener
 * freezes the window for its whole duration — including any notice the window might otherwise
 * have shown about being busy. Work that waits on a network or a disk belongs here instead, and
 * the callback is delivered back on the event dispatch thread, where components may be touched.
 */
public final class BackgroundRequest {
    private BackgroundRequest() {
    }

    /**
     * Runs work off the event dispatch thread and reports back on it.
     *
     * @param work         the work to run away from the event dispatch thread
     * @param whenFinished run on the event dispatch thread once the work ends, told whether it
     *                     completed without throwing
     */
    public static void run(Runnable work, Consumer<Boolean> whenFinished) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                work.run();
                return null;
            }

            @Override
            protected void done() {
                whenFinished.accept(succeeded(this));
            }
        }.execute();
    }

    private static boolean succeeded(SwingWorker<Void, Void> worker) {
        boolean succeeded = true;
        try {
            worker.get();
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            succeeded = false;
        }
        catch (ExecutionException ex) {
            succeeded = false;
        }

        return succeeded;
    }
}
