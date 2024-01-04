import annotations.BindMethod;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dev.webview.Webview;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public final class MethodBinder {
    private final static Gson gson = new Gson();

    public static void bind(Webview wv, Object ...objects) {
        for (Object o : objects) {
            List<Handler> handlers = MethodBinder.createHandlers(o);
            for (Handler h : handlers) {
                if (h == null) continue;
                wv.bind(h.name, WebviewCallbackWrapper.wrap(h));
            }
        }
    }

    private static List<Handler> createHandlers(Object object) {
        List<Handler> handlers = new ArrayList<>();
        for (Method method : object.getClass().getDeclaredMethods()) {
            handlers.add(createHandler(object, method));
        }
        return handlers;
    }

    private static Handler createHandler(Object object, Method method) {
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

    private static List<String> splitArrayToJsonElements(String jsonString) {
        List<String> listElements = new ArrayList<>();

        JsonElement parsedElement = JsonParser.parseString(jsonString);
        if (!parsedElement.isJsonArray()) {
            System.out.println("Error: " + jsonString + " is not a json array.");
            return listElements;
        }

        JsonArray jsonArray = parsedElement.getAsJsonArray();
        return jsonArray.asList().stream().map(JsonElement::toString).toList();
    }

}
