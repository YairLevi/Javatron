import annotations.BindMethod;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public interface MethodBinder {
    String FRONTEND_PATH = "frontend/javatron/methods/";
    Gson gson = new Gson();

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

    static String toJSType(Class<?> c) {
        String type = TypeConverter.jsTypes.get(c.getSimpleName());
        if (type != null) return type;

        return "jt." + c.getSimpleName();
    }

    static void createTypescriptDeclarations(Class<?> c) throws IOException {
        String className = c.getSimpleName();
        String path = FRONTEND_PATH + className + ".d.ts";
        FileManager.createNewFile(path);
        FileWriter writer = new FileWriter(path);

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
            String convertedReturnType = toJSType(method.getReturnType());
            writer.write("export function " + methodName + "(" + argsString + "): Promise<" + convertedReturnType + ">;\n\n");
            System.out.println("----------");
        }

        writer.close();
    }
}
