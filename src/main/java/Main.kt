import classes.Person
import org.slf4j.LoggerFactory

val log = LoggerFactory.getLogger("Main")

fun main() {
    val jt = Javatron()
    jt.setSize(700, 700)
    jt.setTitle("My first Javatron app!")
    jt.setURL("http://localhost:5173")
    jt.bind(Person())
    jt.addBeforeStartCallback { log.info("Started app...") }
    jt.addOnCloseCallback { log.info("Closed the app!") }

    jt.run()
}
