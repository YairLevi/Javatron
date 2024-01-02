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
    Set<String> boundTypes = new HashSet<>();

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
            entry("void", "void"),

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

    static void generateTypes(Object ...objects) {
        try {
            generateTypes((Class<?>[]) Arrays.stream(objects).map(Object::getClass).toArray());
        } catch (IOException e) {
            System.out.println("Failed to create types for frontend.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    static void generateTypes(Class<?>... classes) throws IOException {
        FileWriter types = new FileWriter(TYPES_FILE_PATH, true);
        Arrays.stream(classes).forEach(c -> boundTypes.add(c.getSimpleName()));

        for (Class<?> clazz : classes) {
            // Declare type and export
            String typeName = clazz.getSimpleName();
            types.write("export type " + clazz.getSimpleName() + " = {\n");

            // Add fields and map the types from java to typescript
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String name = field.getName();
                String type = convert(field.getGenericType(), false);
                types.write("\t" + name + ": " + type + "\n");
            }
            types.write("}\n\n");

            System.out.println("Created type: " + typeName);
        }
        types.close();
    }

    /**
     *
     * @param t the java type that needs conversion to javascript.
     * @param addJTPrefix in types.ts, no need to that prefix, but in the methods, we need it because of
     *                    "import * as jt from ../types"
     * @return the converted type name in javascript
     */
    static String convert(Type t, boolean addJTPrefix) {
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
            System.out.println("Java type: " + javaType);
            if (!jsTypes.containsKey(javaType) && !boundTypes.contains(javaType)) {
                System.out.println("Type Error: java type " + javaType + " is not recognized. Did you forget to @BindType ?");
                System.out.println("Used 'any' instead, just in case.");
                type = type.replaceAll(javaType, jsTypes.getOrDefault(javaType, "any"));
            }
            else if (!jsTypes.containsKey(javaType) && addJTPrefix) {
                type = type.replaceAll(javaType, "jt." + javaType);
            }
            else if (jsTypes.containsKey(javaType)) {
                type = type.replaceAll(javaType, jsTypes.get(javaType));
            }
        }
        return type;
    }
}
