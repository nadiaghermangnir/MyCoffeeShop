package ro.ubbcluj.scs.gnir.mycoffeeshop.todo.item

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_item_edit.*
import ro.ubbcluj.scs.gnir.mycoffeeshop.R
import ro.ubbcluj.scs.gnir.mycoffeeshop.core.TAG
import ro.ubbcluj.scs.gnir.mycoffeeshop.todo.data.Item

class ItemEditFragment : Fragment() {
    companion object {
        const val ITEM_ID = "ITEM_ID"
    }

    private lateinit var viewModel: ItemEditViewModel
    private var itemId: String? = null
    private var item: Item? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate")
        arguments?.let {
            if (it.containsKey(ITEM_ID)) {
                itemId = it.getString(ITEM_ID).toString()
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.v(TAG, "onCreateView")
        return inflater.inflate(R.layout.fragment_item_edit, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        setupViewModel()

        fab.setOnClickListener {
            Log.v(TAG, "save item")
            val i = item
            if(i != null) {
                i.name = item_name.text.toString();
                i.quantity = item_quantity.text.toString();
                i.available = item_available.text.toString();
                i.caffeine = item_caffeine.text.toString();
                viewModel.saveOrUpdateItem(i);
            }
        }
        button_delete.setOnClickListener{
            if(item != null){
                viewModel.deleteItem(item!!._id)
            }

        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupViewModel(){
        viewModel = ViewModelProvider(this).get(ItemEditViewModel::class.java)
        viewModel.fetching.observe(viewLifecycleOwner, { fetching ->
            Log.v(TAG, "update fetching")
            progress.visibility = if (fetching) View.VISIBLE else View.GONE
        })
        viewModel.fetchingError.observe(viewLifecycleOwner, { exception ->
            if (exception != null) {
                Log.v(TAG, "update fetching error")
                val message = "Fetching exception ${exception.message}"
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.completed.observe(viewLifecycleOwner) { completed ->
            if (completed) {
                Log.v(TAG, "completed, navigate back")
                findNavController().popBackStack()
            }
        }
        val id = itemId
        if (id == null) {
            item = Item("", "","","","","")
        } else {
            viewModel.getItemById(id).observe(viewLifecycleOwner) {
                Log.v(TAG, "update items")
                if (it != null) {
                    item = it
                    item_name.setText(item!!.name)
                    item_quantity.setText(item!!.quantity.toString())
                    item_available.setText(item!!.available)
                    item_caffeine.setText(item!!.caffeine.toString())

                }
            }
        }
    }

}