import annotations.BindMethod
import org.slf4j.LoggerFactory
import java.io.IOException
import java.io.PrintWriter
import java.util.*
import kotlin.system.exitProcess

object CodeGenerator {
    private const val CLIENT_FOLDER_PATH = "frontend/javatron/"
    private const val METHODS_FOLDER_PATH = CLIENT_FOLDER_PATH + "methods/"
    private const val EVENTS_FOLDER_PATH = CLIENT_FOLDER_PATH + "events/"
    private const val TYPES_FILE_PATH = CLIENT_FOLDER_PATH + "types.ts"

    private val log = LoggerFactory.getLogger(this::class.java)

    init {
        FileManager.createOrReplaceFile(TYPES_FILE_PATH)
        FileManager.createOrReplaceDirectory(METHODS_FOLDER_PATH)
    }

    fun generateTypes(vararg objects: Any) {
        try {
            val writer = PrintWriter(TYPES_FILE_PATH)
            val classes = TypeConverter.getClasses(*objects)
            TypeConverter.boundTypes.addAll(classes.map { it.simpleName })
            for (c in classes) {
                // Declare type and export
                writer.println("export type ${c.simpleName} = {")

                // Add fields and map the types from java to typescript
                for (field in c.declaredFields) {
                    val name = field.name
                    val type = TypeConverter.convert(field.genericType, false)
                    writer.println("\t$name: $type")
                }
                writer.println("}\n")

                log.info("Created type: ${c.simpleName}")
            }
            writer.close()
        } catch (e: IOException) {
            log.error("Failed to generate a types file `types.ts`.", e)
            exitProcess(1)
        }
    }

    fun generateFunctions(vararg objects: Any) {
        for (c in TypeConverter.getClasses(*objects)) {
            createJavascriptFunctions(c)
            createTypescriptDeclarations(c)
        }
    }

    private fun createJavascriptFunctions(c: Class<*>) {
        try {
            val className = c.simpleName
            val path = "$METHODS_FOLDER_PATH$className.js"
            FileManager.createOrReplaceFile(path)
            val writer = PrintWriter(path)

            for (method in c.declaredMethods) {
                if (!method.isAnnotationPresent(BindMethod::class.java)) continue

                val methodName = method.name
                val argsString = method.parameters.joinToString(",") { it.name }
                writer.println("export function $methodName($argsString) {")
                writer.println("\treturn window[\"${className}_$methodName\"]($argsString);")
                writer.println("}\n")
            }

            writer.close()
        } catch (e: IOException) {
            log.error("Failed to create javascript function for class ${c.simpleName}.", e)
            exitProcess(1)
        }
    }

    private fun toJSType(c: Class<*>) =
        TypeConverter.jsTypes[c.simpleName] ?: ("jt." + c.simpleName)

    private fun createTypescriptDeclarations(c: Class<*>) {
        try {
            val path = "$METHODS_FOLDER_PATH${c.simpleName}.d.ts"
            FileManager.createOrReplaceFile(path)
            val writer = PrintWriter(path)

            writer.println("import * as jt from '../types';\n")

            for (method in c.declaredMethods.sortedBy { it.name }) {
                if (!method.isAnnotationPresent(BindMethod::class.java)) continue
                log.info("Created function ${method.name} of class ${c.simpleName}")
                val argsString = method.parameters.joinToString(",") { "${it.name}: ${toJSType(it.type)}" }
                val convertedReturnType = TypeConverter.convert(method.returnType, true)
                var returnTypeString = ": Promise<$convertedReturnType>"
                if (convertedReturnType == "void") returnTypeString = ""
                writer.println("export function ${method.name}($argsString)$returnTypeString;\n")
            }

            writer.close()
        } catch (e: IOException) {
            log.error("Failed to create typescript declarations for class ${c.simpleName}.", e)
            exitProcess(1)
        }
    }
}