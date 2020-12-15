package ro.ubbcluj.scs.gnir.mycoffeeshop.todo.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import ro.ubbcluj.scs.gnir.mycoffeeshop.todo.data.Item


@Dao
interface ItemDao {
    @Query("SELECT * FROM items WHERE userId=:userId ORDER BY name ASC")
    fun getAll(userId: String): LiveData<List<Item>>

    @Query("SELECT * FROM items WHERE _id=:id ")
    fun getById(id: String): LiveData<Item>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Item)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: Item)

    @Query("DELETE FROM items WHERE _id=:id")
    suspend fun delete(id: String)

    @Query("DELETE FROM items")
    suspend fun deleteAll()
}