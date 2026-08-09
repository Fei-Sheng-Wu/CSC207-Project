package database;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Represents an HTTP-based repository implementation.
 */
public abstract class AbstractHttpDataAccessObject {
    protected static final int CODE_OK = 200;
    protected static final int CODE_BAD_REQUEST = 400;

    private static final int TIMEOUT_CONNECT = 10;
    private static final int TIMEOUT_READ = 30;
    private static final int TIMEOUT_WRITE = 10;
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_CONNECT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_WRITE, TimeUnit.SECONDS)
        .build();

    /**
     * Returns the shared HTTP client.
     *
     * @return the shared HTTP client
     */
    protected OkHttpClient getClient() {
        return CLIENT;
    }
}
