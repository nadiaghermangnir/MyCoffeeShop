package ro.ubbcluj.scs.gnir.mycoffeeshop.todo.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import ro.ubbcluj.scs.gnir.mycoffeeshop.core.Constants
import ro.ubbcluj.scs.gnir.mycoffeeshop.todo.data.local.ItemDao
import ro.ubbcluj.scs.gnir.mycoffeeshop.todo.data.remote.ItemApi
import ro.ubbcluj.scs.gnir.mycoffeeshop.core.Result

class ItemRepository(private val itemDao: ItemDao) {

    val items = MediatorLiveData<List<Item>>().apply { postValue(emptyList()) }

    suspend fun refresh(): Result<Boolean> {
        try {
            val itemsApi = ItemApi.service.find()
            items.value = itemsApi;
            for (item in itemsApi) {
                itemDao.insert(item)
            }
            return Result.Success(true)
        } catch(e: Exception) {
            val userId = Constants.instance()?.fetchValueString("_id")
            items.addSource(itemDao.getAll(userId!!)){
                items.value = it
            }
            return Result.Error(e)
        }
    }

    fun getById(itemId: String): LiveData<Item> {
        return itemDao.getById(itemId)
    }

    suspend fun save(item: Item): Result<Item> {
        try {
            val createdItem = ItemApi.service.create(item)
            itemDao.insert(createdItem)
            return Result.Success(createdItem)
        } catch(e: Exception) {
            itemDao.insert(item)
            return Result.Error(e)
        }
    }

    suspend fun update(item: Item): Result<Item> {
        try {
            val updatedItem = ItemApi.service.update(item._id, item)
            itemDao.update(updatedItem)
            return Result.Success(updatedItem)
        } catch(e: Exception) {
            itemDao.update(item)
            return Result.Error(e)
        }
    }

    suspend fun delete(itemId: String): Result<Boolean> {
        try {

            ItemApi.service.delete(itemId)
            itemDao.delete(id = itemId)
            return Result.Success(true)
        } catch (e: Exception) {
            itemDao.delete(id = itemId)
            return Result.Error(e)
        }
    }
}