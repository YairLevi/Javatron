package dev.webview;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.Array;

public class Main {

    public static class Note {
        public String title;
        public String content;
    }

    public static String[] listedJson(String data) {
        return new String[]{data.substring(1, data.length() - 1)};
    }

    public static void main(String[] args) {
        Webview wv = new Webview(true); // Can optionally be created with an AWT component to be painted on.
        ObjectMapper om = new ObjectMapper();
        wv.bind("func", (data) -> {
            System.out.println("Data:\t" + data);
            String jsonString = data.substring(1, data.length() - 1);
            Note note = null;
            try {
                note = om.readValue(jsonString, Note.class);
                System.out.println(note.title + "\n" + note.content);
            } catch (JsonProcessingException e) {
                System.out.println("EXception");
                throw new RuntimeException(e);
            }
            note.title = "new title";
            try {
                return om.writeValueAsString(note);
            } catch (Exception e) {
                System.out.println("seonc ex");
                return "";
            }
        });

        wv.setTitle("My Webview App");
        wv.setSize(800, 600);
        wv.loadURL("http://localhost:5173");

        // Run the webview event loop, the webview is fully disposed when this returns.
        wv.run();
    }

}
