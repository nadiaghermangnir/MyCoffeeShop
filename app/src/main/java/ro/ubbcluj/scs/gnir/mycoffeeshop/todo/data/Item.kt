package ro.ubbcluj.scs.gnir.mycoffeeshop.todo.data

import java.time.LocalDate
import java.util.*


data class Item(
    val id: String,
    var name: String,
    var quantity: Number,
    var available: String? = null,
    var caffeine: Boolean = true,

) {
    override fun toString(): String = "$name $quantity ml "
}
