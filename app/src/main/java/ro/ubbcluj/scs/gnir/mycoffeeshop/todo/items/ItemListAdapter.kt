package ro.ubbcluj.scs.gnir.mycoffeeshop.todo.items

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.view_item.view.*
import ro.ubbcluj.scs.gnir.mycoffeeshop.R
import ro.ubbcluj.scs.gnir.mycoffeeshop.core.TAG
import ro.ubbcluj.scs.gnir.mycoffeeshop.todo.data.Item
import ro.ubbcluj.scs.gnir.mycoffeeshop.todo.item.ItemEditFragment

class ItemListAdapter(
    private val fragment: Fragment
) : RecyclerView.Adapter<ItemListAdapter.ViewHolder>() {

    var items = emptyList<Item>()
        set(value) {
            field = value
            notifyDataSetChanged();
        }

    private var onItemClick: View.OnClickListener;

    init {
        onItemClick = View.OnClickListener { view ->
            val item = view.tag as Item
            fragment.findNavController().navigate(R.id.ItemEditFragment, Bundle().apply {
                putString(ItemEditFragment.ITEM_ID, item.id)
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_item, parent, false)
        Log.v(TAG, "onCreateViewHolder")
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.v(TAG, "onBindViewHolder $position")
        val item = items[position]
        holder.itemView.tag = item
        holder.nameView.text = item.name;
      //  holder.availableView.text = item.available;


        val quantityMl = item.quantity.toString() + "ml";
        holder.quantityView.text = quantityMl;
        if (item.caffeine)
            holder.caffeineView.text = "";
        else if (!item.caffeine)
            holder.caffeineView.text = "DECAF";
        holder.itemView.setOnClickListener(onItemClick)
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameView: TextView = view.name
        val quantityView: TextView = view.quantity
      //  val availableView: TextView = view.available
        val caffeineView: TextView = view.caffeine
    }
}
