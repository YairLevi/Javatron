import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MethodBinding {
    public static List<String> splitArrayToJsonElements(String jsonString) {
        List<String> listElements = new ArrayList<>();

        JsonElement parsedElement = JsonParser.parseString(jsonString);
        if (!parsedElement.isJsonArray()) {
            System.out.println("Error: " + jsonString + " is not a json array.");
            return listElements;
        }

        JsonArray jsonArray = parsedElement.getAsJsonArray();
        return jsonArray.asList().stream().map(JsonElement::toString).toList();
    }

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
