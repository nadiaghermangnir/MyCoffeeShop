package ro.ubbcluj.scs.gnir.mycoffeeshop.todo.items

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ro.ubbcluj.scs.gnir.mycoffeeshop.core.TAG
import ro.ubbcluj.scs.gnir.mycoffeeshop.todo.data.Item
import ro.ubbcluj.scs.gnir.mycoffeeshop.todo.data.ItemRepository
import ro.ubbcluj.scs.gnir.mycoffeeshop.todo.data.local.TodoDatabase
import ro.ubbcluj.scs.gnir.mycoffeeshop.core.Result
import ro.ubbcluj.scs.gnir.mycoffeeshop.todo.data.remote.ItemApi

class ItemsListViewModel(application: Application) : AndroidViewModel(application) {

    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableException = MutableLiveData<Exception>().apply { value = null }

    val items: LiveData<List<Item>>
    val loading: LiveData<Boolean> = mutableLoading
    val loadingError: LiveData<Exception> = mutableException

    private val itemRepository: ItemRepository

    init {
        val itemDao = TodoDatabase.getDatabase(application, viewModelScope).itemDao()
        itemRepository = ItemRepository(itemDao)
        items = itemRepository.items

        CoroutineScope(Dispatchers.Main).launch { collectEvents() }
    }

    suspend fun collectEvents() {
        while (true) {
            val event = ItemApi.RemoteDataSource.eventChannel.receive()
            Log.d("ws", event)
            Log.d("MainActivity", "received $event")
            refresh()
        }
    }

    fun refresh() {
        viewModelScope.launch {
            Log.v(TAG, "refresh...");
            mutableLoading.value = true
            mutableException.value = null
            when (val result = itemRepository.refresh()) {
                is Result.Success -> {
                    Log.d(TAG, "refresh succeeded");
                }
                is Result.Error -> {
                    Log.w(TAG, "refresh failed", result.exception);
                    mutableException.value = result.exception
                }
            }
            mutableLoading.value = false
        }
    }
}
