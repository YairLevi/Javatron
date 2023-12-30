import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

import static java.util.Map.entry;

public class TypeGenerator {
    private static final String TYPES_FILE_PATH = "frontend/javatron/types.ts";
    private static final Map<String, String> javaToTypescriptTypes = Map.ofEntries(
            entry("String", "string"),
            entry("int", "number"),
            entry("float", "number"),
            entry("double", "number"),
            entry("boolean", "boolean")
    );

    static { createTypesFileIfNeeded(); }

    private static void createTypesFileIfNeeded() {
        try {
            File file = new File(TYPES_FILE_PATH);
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

    public static void createTypeFromClass(Class<?> clazz) throws IOException {
        FileWriter types = new FileWriter(TYPES_FILE_PATH, true);

        // Declare type and export
        String typeName = clazz.getSimpleName();
        types.write("export type " + clazz.getSimpleName() + " = {\n");

        // Add fields and map the types from java to typescript
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            String type = javaToTypescriptTypes.get(field.getType().getSimpleName());
            types.write("\t" + name + ": " + type + "\n");
        }
        types.write("}\n");
        types.close();

        System.out.println("Created type: " + typeName);
    }

}
