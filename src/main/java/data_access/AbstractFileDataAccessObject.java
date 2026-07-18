package data_access;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Represents a file-based data access object implementation.
 */
public abstract class AbstractFileDataAccessObject {
    protected static final String DIRECTORY_SUITABLE = "suitable";

    /**
     * Returns the resolved path of the specified file in the OS's user data directory and ensures the file exists.
     *
     * @param file the name of the file
     * @return the resolved path
     * @throws RuntimeException if the file cannot be created
     */
    protected Path getPath(String file) {
        final Path path = Paths.get(System.getProperty("user.home"), DIRECTORY_SUITABLE, file);
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return path;
    }
}
