import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FileManager {
    public static void createOrReplaceDirectory(String pathString) {
        Path path = Path.of(pathString);

        try {
            // Delete the directory if it exists
            if (Files.exists(path)) {
                Files.walk(path)
                        .sorted(java.util.Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            }

            // Create the directory
            Files.createDirectories(path);
        } catch (IOException e) {
            System.err.println("Error occurred while creating or replacing directory: " + e.getMessage());
        }
    }

    /**
     * Creates a new file at `path`, and overrides existing files.
     * @param path
     */
    public static void createNewFile(String path) {
        try {
            File file = new File(path);
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                if (!parentDir.mkdirs()) {
                    System.out.println("Failed to create directories.");
                    throw new IOException("Failed to create parent dirs for file.");
                }
            }
            if (!file.createNewFile()) {
                file.delete();
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creating a types file for the frontend.");
            System.out.println("Error: " + e.getMessage());
        }
    }
}
