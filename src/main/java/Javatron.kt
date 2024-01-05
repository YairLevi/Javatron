import CodeGenerator.generateEventsAPI
import CodeGenerator.generateFunctions
import CodeGenerator.generateTypes
import dev.webview.Webview
import java.util.*
import java.util.function.Consumer

class Javatron (withDevTools: Boolean = true) {
    private val _webview: Webview = Webview(withDevTools)
    private val _beforeStartCallbacks: MutableList<Runnable> = ArrayList()
    private val _onCloseCallbacks: MutableList<Runnable> = ArrayList()
    private val _bindObjects: MutableList<Any> = ArrayList()
    private var _url: String = ""

    // data url for loading HTML, until webview_java is updated to include setHTML().
    // data:text/html,<!DOCTYPE html><html>blah</html>

    init {
        setSize(800, 600)
    }

    fun setURL(url: String) {
        _url = url
    }

    fun setTitle(title: String) {
        _webview.setTitle(title)
    }

    fun setSize(width: Int, height: Int) {
        _webview.setSize(width, height)
    }

    fun setMinSize(minWidth: Int, minHeight: Int) {
        _webview.setMinSize(minWidth, minHeight)
    }

    fun setMaxSize(maxWidth: Int, maxHeight: Int) {
        _webview.setMaxSize(maxWidth, maxHeight)
    }

    fun setFixedSize(fixedWidth: Int, fixedHeight: Int) {
        _webview.setFixedSize(fixedWidth, fixedHeight)
    }

    fun bind(vararg objects: Any) {
        _bindObjects.addAll(Arrays.stream(objects).toList())
    }

    fun addBeforeStartCallback(r: Runnable) {
        _beforeStartCallbacks.add(r)
    }

    fun addOnCloseCallback(r: Runnable) {
        _onCloseCallbacks.add(r)
    }

    fun run() {
        generateEventsAPI()
        generateTypes(*_bindObjects.toTypedArray())
        generateFunctions(*_bindObjects.toTypedArray())
        MethodBinder.bind(_webview, *_bindObjects.toTypedArray())

        _beforeStartCallbacks.forEach(Consumer { it.run() })
        _webview.loadURL(_url)
        _webview.run()
        _onCloseCallbacks.forEach(Consumer { it.run() })
        _webview.close()
    }
}