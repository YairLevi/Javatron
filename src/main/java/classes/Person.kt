package classes

import Ipc
import annotations.BindMethod
import annotations.BindType

@BindType
class Person(
    val name: String = "",
    var age: Int = 0,
    val hobbies: List<String> = emptyList(),
    val string: Map<Person, List<Person>> = emptyMap(),
) {


    @BindMethod
    fun addTwoNumbers(a: Int, b: Int): Int {
        return a + b;
    }

    @BindMethod
    fun incrementAndPrint() {
        age++;
        println("My age increased to $age")
        println("invoking event...")
        Ipc.invoke("event")
    }
}
