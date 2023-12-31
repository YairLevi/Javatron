import java.io.File;
import java.io.IOException;

public class FileManager {
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
