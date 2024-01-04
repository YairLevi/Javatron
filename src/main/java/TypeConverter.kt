import org.slf4j.LoggerFactory
import java.lang.reflect.Type
import java.util.*
import java.util.regex.Pattern

interface TypeConverter {
    companion object {

        private val log = LoggerFactory.getLogger(this::class.java)

        val boundTypes: MutableSet<String> = HashSet()

        val jsTypes: Map<String, String> = mapOf(
            // primitives
            "byte" to "number",
            "byte" to "number",
            "char" to "number",
            "short" to "number",
            "int" to "number",
            "long" to "number",
            "float" to "number",
            "double" to "number",
            "boolean" to "boolean",
            "String" to "string",
            "void" to "void",

            // classes
            "Byte" to "number",
            "Character" to "number",
            "Short" to "number",
            "Integer" to "number",
            "Long" to "number",
            "Float" to "number",
            "Double" to "number",
            "Boolean" to "boolean",
            "List" to "Array"
        )

        fun getClasses(vararg objects: Any) =
            objects.map { it.javaClass }

        /**
         *
         * @param t the java type that needs conversion to javascript.
         * @param addJTPrefix in types.ts, no need to that prefix, but in the methods, we need it because of
         * "import * as jt from ../types"
         * @return the converted type name in javascript
         */
        fun convert(t: Type, addJTPrefix: Boolean): String? {
            var type = t.typeName
                // Remove java packages prefix
                .replace("\\b[a-z]+\\.".toRegex(), "")
                // Remove part of classes that are a specific implementation of a generic class (List, etc.)
                .replace("Tree|Hash|Linked|Array".toRegex(), "")

            val pattern = Pattern.compile("[a-zA-Z0-9]+")
            val matcher = pattern.matcher(type)

            // Switch each java type with its corresponding typescript type, or "any".
            // do this by walking through all words in the type string.
            // don't traverse the jsTypes.keySet() because it can miss some unknown types in the string.
            // "System" for example won't be found in the keys, so it will remain System, and will not be "any".
            while (matcher.find()) {
                val javaType = matcher.group()
                if (!jsTypes.containsKey(javaType) && !boundTypes.contains(javaType)) {
                    log.error("java type $javaType is not recognized. Did you forget to @BindType ?\n" +
                            "Used 'any' instead, just in case.")
                    type = type.replace(javaType, jsTypes.getOrDefault(javaType, "any"))
                } else if (!jsTypes.containsKey(javaType) && addJTPrefix) {
                    type = type.replace(javaType, "jt.$javaType")
                } else if (jsTypes.containsKey(javaType)) {
                    type = type.replace(javaType, jsTypes[javaType]!!)
                }
            }
            return type
        }
    }
}