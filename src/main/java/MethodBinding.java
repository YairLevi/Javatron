import annotations.BindMethod;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface MethodBinding {
    String FRONTEND_PATH = "frontend/javatron/methods/";
    Gson gson = new Gson();

    /*
    Order of actions to create a handler:
    1- get a method with @BindMethod.
    2- get class name as "className".
    3- get method name as "methodName".
    4- set name of handler to be "className_methodName".
    5- handler:

    6- separate jsonString array to params in string format.
    7- get parameter types of method.
    8- convert each param string to proper param type and put into Object[] array.
    9- invoke method with the Object[] array and get return type.
    10- convert return value to gson string, and return.
     */
    static List<Handler> createHandlers(Object object) {
        List<Handler> handlers = new ArrayList<>();
        for (Method method : object.getClass().getDeclaredMethods()) {
            handlers.add(createHandler(object, method));
        }
        return handlers;
    }

    static Handler createHandler(Object object, Method method) {
        if (!method.isAnnotationPresent(BindMethod.class)) return null;

        Handler handler = new Handler();
        String className = object.getClass().getSimpleName();
        String methodName = method.getName();
        handler.name = className + "_" + methodName;
        handler.callback = jsonArgs -> {
            List<String> jsonElements = splitArrayToJsonElements(jsonArgs);
            jsonElements.forEach(System.out::println);
            Parameter[] params = method.getParameters();
            List<Object> properParams = new ArrayList<>();
            for (int i = 0; i < params.length; i++) {
                String currentJsonElement = jsonElements.get(i);
                Parameter currentParam = params[i];
                properParams.add(gson.fromJson(currentJsonElement, currentParam.getType()));
            }
            try {
                return  method.invoke(object, properParams.toArray());
            } catch (Exception e) {
                System.out.println("Failed to execute handler for " + handler.name);
                System.out.println("Error: " + e.getMessage());
            }
            return null;
        };

        return handler;
    }

    static List<String> splitArrayToJsonElements(String jsonString) {
        List<String> listElements = new ArrayList<>();

        JsonElement parsedElement = JsonParser.parseString(jsonString);
        if (!parsedElement.isJsonArray()) {
            System.out.println("Error: " + jsonString + " is not a json array.");
            return listElements;
        }

        JsonArray jsonArray = parsedElement.getAsJsonArray();
        return jsonArray.asList().stream().map(JsonElement::toString).toList();
    }

    static void createJavascriptFunctions(Class<?> c) throws IOException {
        String className = c.getSimpleName();
        String path = FRONTEND_PATH + className + ".js";
        FileManager.createNewFile(path);
        FileWriter writer = new FileWriter(path);

        for (Method method : c.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(BindMethod.class)) continue;

            String methodName = method.getName();
            Parameter[] params = method.getParameters();
            String argsString = Arrays.stream(params).map(Parameter::getName).collect(Collectors.joining(","));
            writer.write("export function " + methodName + "(" + argsString + ") {\n");
            writer.write("\treturn window[\"" + className + "_" + methodName + "\"](" + argsString + ");\n");
            writer.write("}\n\n");
        }

        writer.close();
    }

//    static void createMethods(Class<?> c) throws IOException {
//        String className = c.getSimpleName();
//
//        String jsPath = "frontend/javatron/methods/" + className + ".js";
//        String tsPath = "frontend/javatron/methods/" + className + ".d.ts";
//
//        FileManager.createNewFile(jsPath);
//        FileManager.createNewFile(tsPath);
//
//        FileWriter javascriptWriter = new FileWriter(jsPath);
//        FileWriter typescriptWriter = new FileWriter(tsPath);
//
//        Method[] methods = c.getDeclaredMethods();
//        for (Method method : methods) {
//
//        }
//    }

//    public static void createType(Class<?> c) {
//        if (c.isAnnotationPresent(BindType.class)) {
//            try {
////                TypeGenerator.createTypeFromClass(c);
//                System.out.println("Blah blah, creating type");
//            } catch (Exception e) {
//                System.out.println("Error in bind: Failed to create type from class " + c.getSimpleName());
//            }
//        } else {
//            System.out.println("Class " + c.getSimpleName() + " is not annotated by @BindType");
//        }
//    }
//
//    private static BindCallback createBindCallback(Object object, Method method) {
//        if (!method.isAnnotationPresent(BindMethod.class)) {
//            System.out.println("Method " + method.getName() + " is not bindable.");
//            return null;
//        }
//        BindCallback bc = new BindCallback();
//        bc.name = object.getClass().getSimpleName() + "_" + method.getName();
//
//        WebviewBindCallback callback = jsonArgs -> {
//            // Convert the json args to proper types args
//            try {
//                List<String> jsonArgsSeparated = new ArrayList<>();//splitJsonAtTopLevel(mapper.readTree(jsonArgs));
//                Class<?>[] fields = method.getParameterTypes();
//                List<Object> params = new ArrayList<>();
//                if (fields.length != jsonArgsSeparated.size()) {
//                    System.out.println("Error: invalid number of params:");
//                    // error log, method name, param types and names...
//                    return null;
//                }
//
//                for (int i = 0; i < fields.length; i++) {
//                    Object param = mapper.readValue(jsonArgsSeparated.get(i), fields[i].getComponentType());
//                    params.add(param);
//                }
//
//                method.invoke(object, params);
//
//            } catch (Exception e) {
//                System.out.println(e.getMessage());
//            }
//            return null;
//        };
//
//        bc.callback = callback;
//        return bc;
//    }
//
//    public static List<BindCallback> bind(Object[] objects) {
//        List<BindCallback> binds = new ArrayList<>();
//
//        for (Object object : objects) {
//            Class<?> c = object.getClass();
//            createType(c);
//
//            for (Method method : c.getDeclaredMethods()) {
//                BindCallback bc = createBindCallback(object, method);
//                if (bc == null) continue;
//                binds.add(bc);
//            }
//        }
//        return binds;
//    }
}
