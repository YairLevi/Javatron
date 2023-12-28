package dev.webview;

public class Main {

    public static void main(String[] args) {
        Webview wv = new Webview(true); // Can optionally be created with an AWT component to be painted on.

        // Calling `await echo(1,2,3)` will return `[1,2,3]`
        wv.bind("echo", (arguments) -> {
            return arguments;
        });

        wv.bind("test", (arguments) -> {
            for (String arg : arguments) {

            }
            return "";
        });

        wv.setTitle("My Webview App");
        wv.setSize(800, 600);
        wv.loadURL("http://localhost:5173");

        // Run the webview event loop, the webview is fully disposed when this returns.
        wv.run();
    }

}
