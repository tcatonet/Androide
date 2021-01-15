package projet


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(private val items: List<InfoItem>, var clickListner: OnItemClickListener):RecyclerView.Adapter<ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(inflater, parent)    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item,clickListner)
    }

    override fun getItemCount(): Int = items.size
}
interface OnItemClickListener {
    fun onItemClick(item:InfoItem,position: Int)
}