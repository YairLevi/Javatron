import annotations.BindMethod;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

public final class CodeGenerator {
    private final static String CLIENT_FOLDER_PATH = "frontend/javatron/";
    private final static String METHODS_FOLDER_PATH = CLIENT_FOLDER_PATH + "methods/";
    private final static String TYPES_FILE_PATH = CLIENT_FOLDER_PATH + "types.ts";

    static {
        FileManager.createNewFile(TYPES_FILE_PATH);
        FileManager.createOrReplaceDirectory(METHODS_FOLDER_PATH);
    }

    public static void generateTypes(Object... objects) {
        FileWriter writer = null;
        try {
            Class<?>[] classes = TypeConverter.getClasses(objects);
            writer = new FileWriter(TYPES_FILE_PATH, true);
            Arrays.stream(classes).forEach(c -> TypeConverter.boundTypes.add(c.getSimpleName()));

            for (Class<?> clazz : classes) {
                // Declare type and export
                String typeName = clazz.getSimpleName();
                writer.write("export type " + clazz.getSimpleName() + " = {\n");

                // Add fields and map the types from java to typescript
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    String name = field.getName();
                    String type = TypeConverter.convert(field.getGenericType(), false);
                    writer.write("\t" + name + ": " + type + "\n");
                }
                writer.write("}\n\n");

                System.out.println("Created type: " + typeName);
            }
        } catch (IOException e) {
            System.out.println("Failed to create types for frontend.");
            e.printStackTrace();
            System.exit(1);
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (IOException closeErr) {
                System.out.println("Failed to close writer.");
                closeErr.printStackTrace();
            }
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
        FileWriter writer = null;
        try {
            String className = c.getSimpleName();
            String path = METHODS_FOLDER_PATH + className + ".js";
            FileManager.createNewFile(path);
            writer = new FileWriter(path);

            for (Method method : c.getDeclaredMethods()) {
                if (!method.isAnnotationPresent(BindMethod.class)) continue;

                String methodName = method.getName();
                Parameter[] params = method.getParameters();
                String argsString = Arrays.stream(params).map(Parameter::getName).collect(Collectors.joining(","));
                writer.write("export function " + methodName + "(" + argsString + ") {\n");
                writer.write("\treturn window[\"" + className + "_" + methodName + "\"](" + argsString + ");\n");
                writer.write("}\n\n");
            }
        } catch (IOException e) {
            System.out.println("Failed to create javascript function for class " + c.getSimpleName() + ".");
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException closeErr) {
                System.out.println("Failed to close writer");
                closeErr.printStackTrace();
            }
        }
    }

    private static String toJSType(Class<?> c) {
        String type = TypeConverter.jsTypes.get(c.getSimpleName());
        if (type != null) return type;

        return "jt." + c.getSimpleName();
    }

    private static void createTypescriptDeclarations(Class<?> c) {
        FileWriter writer = null;
        try {
            String className = c.getSimpleName();
            String path = METHODS_FOLDER_PATH + className + ".d.ts";
            FileManager.createNewFile(path);
            writer = new FileWriter(path);

            writer.write("import * as jt from '../types';\n\n");

            for (Method method : Arrays.stream(c.getDeclaredMethods()).sorted(Comparator.comparing(Method::getName)).toList()) {
                if (!method.isAnnotationPresent(BindMethod.class)) continue;
                System.out.println(method.getName());
                String methodName = method.getName();
                Parameter[] params = method.getParameters();
                String argsString = Arrays.stream(params)
                        .map(p -> {
                            String name = p.getName();
                            String type = toJSType(p.getType());
                            System.out.println("type: " + type);
                            return name + ": " + type;
                        })
                        .collect(Collectors.joining(","));
                String convertedReturnType = TypeConverter.convert(method.getReturnType(), true);
                String returnTypeString = convertedReturnType.equals("void") ? "" : ": Promise<" + convertedReturnType + ">";
                writer.write("export function " + methodName + "(" + argsString + ")" + returnTypeString + ";\n\n");
                System.out.println("----------");
            }
        } catch (IOException e) {
            System.out.println("Failed to create typescript declarations for class " + c.getSimpleName() + ".");
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException closeErr) {
                System.out.println("Failed to close writer.");
                closeErr.printStackTrace();
            }
        }
    }
}
