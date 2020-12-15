package ro.ubbcluj.scs.gnir.mycoffeeshop


import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ro.ubbcluj.scs.gnir.mycoffeeshop.core.Constants
import ro.ubbcluj.scs.gnir.mycoffeeshop.core.TAG
import ro.ubbcluj.scs.gnir.mycoffeeshop.todo.data.remote.ItemApi

class MainActivity : AppCompatActivity() {
    var isActive = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        Constants.instance(this.applicationContext);
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        isActive = true
        CoroutineScope(Dispatchers.Main).launch { collectEvents() }
    }

    override fun onStop() {
        super.onStop()
        isActive = false
    }

    private suspend fun collectEvents() {
        while (isActive) {
            val event = ItemApi.RemoteDataSource.eventChannel.receive()
        }
    }
}
