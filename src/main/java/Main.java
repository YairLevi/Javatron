public class Main {
    public static void main(String[] args) {

        Javatron jt = new Javatron();
        jt.setSize(700, 700);
        jt.setTitle("My first Javatron app!");
        jt.setURL("http://localhost:5173");

        TestClass t = new TestClass();
        Custom c = new Custom();

        jt.bind(c, t);
        jt.addBeforeStartCallback(() -> System.out.println("Started app..."));
        jt.addOnCloseCallback(() -> System.out.println("Closed the app!"));

        jt.run();
    }
}
