import classes.Custom;
import classes.TestClass;

public class JavatronApp {

    public static void main(String[] args) {
        Javatron jt = new Javatron();
        jt.setSize(500, 500);
        jt.setTitle("My first Javatron app!");

        TestClass t = new TestClass();
        Custom c = new Custom();

        jt.bind(c, t);
        jt.addBeforeStartCallback(() -> System.out.println("Started app..."));
        jt.addOnCloseCallback(() -> System.out.println("Closed the app!"));

        jt.run();
    }
}
