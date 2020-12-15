package ro.ubbcluj.scs.gnir.mycoffeeshop.todo.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "items")
data class Item(
    @PrimaryKey @ColumnInfo(name = "_id") val _id: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "userId") var userId: String,
    @ColumnInfo(name = "quantity") var quantity: String,
    @ColumnInfo(name = "available") var available: String,
    @ColumnInfo(name = "caffeine") var caffeine: String,

    ) {
    override fun toString(): String = name + " " + quantity + "ml";
}
