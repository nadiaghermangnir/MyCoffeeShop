package ro.ubbcluj.scs.gnir.mycoffeeshop.todo.item

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ro.ubbcluj.scs.gnir.mycoffeeshop.core.TAG
import ro.ubbcluj.scs.gnir.mycoffeeshop.todo.data.Item
import ro.ubbcluj.scs.gnir.mycoffeeshop.todo.data.ItemRepository
import ro.ubbcluj.scs.gnir.mycoffeeshop.todo.data.local.TodoDatabase
import  ro.ubbcluj.scs.gnir.mycoffeeshop.core.Result

class ItemEditViewModel(application: Application) : AndroidViewModel(application) {


    private val mutableFetching = MutableLiveData<Boolean>().apply { value = false }
    private val mutableCompleted = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

   //val item: LiveData<Item> = mutableItem
    val fetching: LiveData<Boolean> = mutableFetching
    val fetchingError: LiveData<Exception> = mutableException
    val completed: LiveData<Boolean> = mutableCompleted

    val itemRepository: ItemRepository

    init {
        val itemDao = TodoDatabase.getDatabase(application, viewModelScope).itemDao()
        itemRepository = ItemRepository(itemDao)
    }

    fun getItemById(itemId: String): LiveData<Item> {
        Log.v(TAG, "getItemById...")
        return itemRepository.getById(itemId)
    }


    fun saveOrUpdateItem(item: Item) {
        viewModelScope.launch {
            Log.v(TAG, "saveOrUpdateItem...");
            mutableFetching.value = true
            mutableException.value = null
            val result: Result<Item>
            if (item._id.isNotEmpty()) {
                result = itemRepository.update(item)
            } else {
                result = itemRepository.save(item)
            }
            when (result) {
                is Result.Success -> {
                    Log.d(TAG, "saveOrUpdateItem succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "saveOrUpdateItem failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableCompleted.value = true
            mutableFetching.value = false
        }
    }

    fun deleteItem(itemId: String)
        {
            viewModelScope.launch {
                mutableFetching.value = true
                mutableException.value = null
                val result: Result<Boolean> = itemRepository.delete(itemId)
                when (result) {
                    is Result.Success -> {
                        Log.d(TAG, "delete succeeded");
//                    mutableItem.value = result.data
                    }
                    is Result.Error -> {
                        Log.w(TAG, "delete failed", result.exception);
                        mutableException.value = result.exception
                    }
                }
                mutableCompleted.value = true
                mutableFetching.value = false
            }
        }
}
