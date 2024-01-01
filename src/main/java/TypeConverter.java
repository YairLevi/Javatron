import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Map.entry;

public interface TypeConverter {
    String TYPES_FILE_PATH = "frontend/javatron/types.ts";

    Map<String, String> jsTypes = Map.ofEntries(
            // primitives
            entry("byte", "number"),
            entry("char", "number"),
            entry("short", "number"),
            entry("int", "number"),
            entry("long", "number"),
            entry("float", "number"),
            entry("double", "number"),
            entry("boolean", "boolean"),
            entry("String", "string"),

            // classes
            entry("Byte", "number"),
            entry("Character", "number"),
            entry("Short", "number"),
            entry("Integer", "number"),
            entry("Long", "number"),
            entry("Float", "number"),
            entry("Double", "number"),
            entry("Boolean", "boolean"),
            entry("List", "Array")
    );

    static void createTypeFromClass(Class<?>... classes) throws IOException {
        FileWriter types = new FileWriter(TYPES_FILE_PATH, true);
        for (Class<?> clazz : classes) {
            // Declare type and export
            String typeName = clazz.getSimpleName();
            types.write("export type " + clazz.getSimpleName() + " = {\n");

            // Add fields and map the types from java to typescript
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String name = field.getName();
                String type = jsTypes.get(field.getType().getSimpleName());
                types.write("\t" + name + ": " + type + "\n");
            }
            types.write("}\n\n");

            System.out.println("Created type: " + typeName);
        }

        types.close();

    }

    static String convert(Type t) {
        String type = t.getTypeName();

        // Remove java builtin classes package prefix
        type = type.replaceAll("java\\.lang\\.|java\\.util\\.", "");

        // Remove part of classes that are a specific implementation of a generic class (List, etc.)
        type = type.replaceAll("Tree|Hash|Linked|Array", "");

        Pattern pattern = Pattern.compile("[a-zA-Z0-9]+");
        Matcher matcher = pattern.matcher(type);

        // Switch each java type with its corresponding typescript type, or "any".
        // do this by walking through all words in the type string.
        // don't traverse the jsTypes.keySet() because it can miss some unknown types in the string.
        // "System" for example won't be found in the keys, so it will remain System, and will not be "any".
        while (matcher.find()) {
            String javaType = matcher.group();
            type = type.replaceAll(javaType, jsTypes.getOrDefault(javaType, "any"));
        }
        return type;
    }
}
