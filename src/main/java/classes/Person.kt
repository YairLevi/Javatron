package classes

import annotations.BindMethod
import annotations.BindType

@BindType
class Person(
    val name: String = "",
    val age: Int = 0,
    val hobbies: List<String> = emptyList(),
    val string: Map<Person, List<Person>> = emptyMap(),
) {


    @BindMethod
    fun DoSomething(m: Map<Long, List<Person>>): Map<Long, List<Person>> {
        return emptyMap()
    }

    @BindMethod
    fun Second(l: List<Person>): List<String> {
        return emptyList()
    }
}
