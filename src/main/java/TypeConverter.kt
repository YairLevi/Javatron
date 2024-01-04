import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Map.entry;

public interface TypeConverter {
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

    static Class<?>[] getClasses(Object ...objects) {
        return Arrays.stream(objects)
                .map(Object::getClass)
                .toArray(Class[]::new);
    }

    static String extractTypeName(String fullTypeName) {
        // Split the full type name based on the dot (.) separator
        String[] parts = fullTypeName.split("\\.");

        // The last part of the split array is the type name
        String typeName = parts[parts.length - 1];

        return typeName;
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
        // Remove java packages prefix
        type = type.replaceAll("\\b[a-z]+\\.", "");
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
