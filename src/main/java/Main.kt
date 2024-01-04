import org.slf4j.LoggerFactory

val log = LoggerFactory.getLogger("Main")
fun main(args: Array<String>) {
    val jt = Javatron()
    jt.setSize(700, 700)
    jt.setTitle("My first Javatron app!")
    jt.url = "http://localhost:5173"

    val t = TestClass()
    val c = Custom()

    jt.bind(c, t)
    jt.addBeforeStartCallback { log.info("Started app...") }
    jt.addOnCloseCallback { log.info("Closed the app!") }

    jt.run()
}
