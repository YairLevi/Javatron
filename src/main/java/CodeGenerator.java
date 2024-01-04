import annotations.BindMethod;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public final class CodeGenerator {
    private final static String CLIENT_FOLDER_PATH = "frontend/javatron/";
    private final static String METHODS_FOLDER_PATH = CLIENT_FOLDER_PATH + "methods/";
    private final static String EVENTS_FOLDER_PATH = CLIENT_FOLDER_PATH + "events/";
    private final static String TYPES_FILE_PATH = CLIENT_FOLDER_PATH + "types.ts";

    static {
        FileManager.createOrReplaceFile(TYPES_FILE_PATH);
        FileManager.createOrReplaceDirectory(METHODS_FOLDER_PATH);
    }

    public static void generateTypes(Object... objects) {
        try {
            PrintWriter writer = new PrintWriter(TYPES_FILE_PATH);
            Class<?>[] classes = TypeConverter.getClasses(objects);
            Arrays.stream(classes).forEach(c -> TypeConverter.boundTypes.add(c.getSimpleName()));
            for (Class<?> c : classes) {
                // Declare type and export
                String typeName = c.getSimpleName();
                writer.println("export type " + c.getSimpleName() + " = {");

                // Add fields and map the types from java to typescript
                Field[] fields = c.getDeclaredFields();
                for (Field field : fields) {
                    String name = field.getName();
                    String type = TypeConverter.convert(field.getGenericType(), false);
                    writer.println("\t" + name + ": " + type);
                }
                writer.println("}\n");

                Log.INFO("Created type: %s", typeName);
            }
            writer.close();
        } catch (IOException e) {
            Log.SEVERE("Failed to generate a types file `types.ts`.");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void generateFunctions(Object... objects) {
        Class<?>[] classes = TypeConverter.getClasses(objects);
        for (Class<?> c : classes) {
            createJavascriptFunctions(c);
            createTypescriptDeclarations(c);
        }
    }

    private static void createJavascriptFunctions(Class<?> c) {
        try {
            String className = c.getSimpleName();
            String path = METHODS_FOLDER_PATH + className + ".js";
            FileManager.createOrReplaceFile(path);
            PrintWriter writer = new PrintWriter(path);

            for (Method method : c.getDeclaredMethods()) {
                if (!method.isAnnotationPresent(BindMethod.class)) continue;

                String methodName = method.getName();
                Parameter[] params = method.getParameters();
                String argsString = Arrays.stream(params).map(Parameter::getName).collect(Collectors.joining(","));
                writer.println("export function " + methodName + "(" + argsString + ") {");
                writer.println("\treturn window[\"" + className + "_" + methodName + "\"](" + argsString + ");");
                writer.println("}\n");
            }

            writer.close();
        } catch (IOException e) {
            Log.SEVERE("Failed to create javascript function for class %s.", c.getSimpleName());
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static String toJSType(Class<?> c) {
        String type = TypeConverter.jsTypes.get(c.getSimpleName());
        if (type != null) return type;

        return "jt." + c.getSimpleName();
    }

    private static void createTypescriptDeclarations(Class<?> c) {
        try {
            String className = c.getSimpleName();
            String path = METHODS_FOLDER_PATH + className + ".d.ts";
            FileManager.createOrReplaceFile(path);
            PrintWriter writer = new PrintWriter(path);

            writer.println("import * as jt from '../types';\n");

            for (Method method : Arrays.stream(c.getDeclaredMethods()).sorted(Comparator.comparing(Method::getName)).toList()) {
                if (!method.isAnnotationPresent(BindMethod.class)) continue;
                Log.INFO("Created function %s of class %s", method.getName(), c.getSimpleName());
                String methodName = method.getName();
                Parameter[] params = method.getParameters();
                String argsString = Arrays.stream(params)
                        .map(p -> {
                            String name = p.getName();
                            String type = toJSType(p.getType());
                            return name + ": " + type;
                        })
                        .collect(Collectors.joining(","));

                String convertedReturnType = TypeConverter.convert(method.getReturnType(), true);
                String returnTypeString = "Promise<" + convertedReturnType + ">";
                if (convertedReturnType.equals("void"))
                    returnTypeString = "";

                writer.println("export function " + methodName + "(" + argsString + ")" + returnTypeString + ";\n");
            }

            writer.close();
        } catch (IOException e) {
            Log.SEVERE("Failed to create typescript declarations for class %s.", c.getSimpleName());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
