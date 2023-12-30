import dev.webview.Webview;

public class Main {
    private static final String ClientURL = "http://localhost:5173";

    public static void main(String[] args) {
        Webview wv = new Webview(true);
        wv.bind("echo", jsonElements -> {
            System.out.println(jsonElements);
            return jsonElements;
        });

        wv.setSize(700, 1000);
        wv.setTitle("Javatron test");
        wv.loadURL(ClientURL);
        wv.run();
    }
}
