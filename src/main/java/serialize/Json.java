package serialize;

import com.google.gson.Gson;

public class Json {
    private static final Gson gson = new Gson();

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(String jsonString, Class<T> c) {
        return gson.fromJson(jsonString, c);
    }
}
