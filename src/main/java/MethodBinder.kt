import annotations.BindMethod
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import dev.webview.Webview
import org.slf4j.LoggerFactory
import java.lang.reflect.Method
import java.util.function.Consumer

object MethodBinder {
    private val gson = Gson()
    private val log = LoggerFactory.getLogger(this::class.java)

    fun bind(wv: Webview, vararg objects: Any) {
        for (o in objects) {
            val handlers = createHandlers(o)
            for (h in handlers) {
                if (h == null) continue

                wv.bind(h.name, WebviewCallbackWrapper.wrap(h))
            }
        }
    }

    private fun createHandlers(obj: Any): List<Handler?> {
        val handlers: MutableList<Handler?> = ArrayList()
        for (method in obj.javaClass.declaredMethods) {
            handlers.add(createHandler(obj, method))
        }
        return handlers
    }

    private fun createHandler(obj: Any, method: Method): Handler? {
        if (!method.isAnnotationPresent(BindMethod::class.java)) return null
        val name = obj.javaClass.simpleName + "_" + method.name
        return Handler(
            name
        ) { jsonArgs: String ->
            val jsonElements = splitArrayToJsonElements(jsonArgs)
            jsonElements.forEach(Consumer { x: String? -> println(x) })
            val params = method.parameters
            val properParams: MutableList<Any> = ArrayList()
            var i = 0
            while (i < params.size) {
                val currentJsonElement = jsonElements[i]
                val currentParam = params[i]
                properParams.add(gson.fromJson(currentJsonElement, currentParam.type))
                i++
            }
            try {
                method.invoke(obj, *properParams.toTypedArray())
            } catch (e: Exception) {
                log.error("Failed to execute handler for $name", e)
                null
            }
        }
    }

    private fun splitArrayToJsonElements(jsonString: String): List<String> {
        val parsedElement = JsonParser.parseString(jsonString)
        if (!parsedElement.isJsonArray) {
            log.error("$jsonString is not a json array.")
            return ArrayList()
        }
        return parsedElement.asJsonArray.map { it.toString() }
    }
}