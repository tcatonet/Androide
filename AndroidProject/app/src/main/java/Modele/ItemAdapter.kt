package Modele


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

//Liaison entre une recyclerView et les données de la liste
class ItemAdapter(private val items: List<InfoItem>, var clickListner: OnItemClickListener):RecyclerView.Adapter<ItemViewHolder>() {

        //Renvoie un itemViewHolder autant de fois qu'il y a d'item dans la liste
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(inflater, parent)    }

    //Pour afficher graphiquement un item, on récupère l'item à la bonne position
    // et on lie cette item avec le viewHolder
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item,clickListner)
    }

    //Combien d'items se trouvent dans la liste
    override fun getItemCount(): Int = items.size
}

interface OnItemClickListener {
    fun onItemClick(item: InfoItem, position: Int)
}