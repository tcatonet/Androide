package RecyclerView


import Modele.InfoItem
import Modele.OnItemClickListener
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R

//Représente visuellement un élément de la liste
class ItemViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(
        inflater.inflate(R.layout.user_item, parent, false) //On envoie un layout user_item au constructeur de la classe parent
    ) {

    private var fullNameTextView: TextView? = null
    private var descriptionTextView: TextView? = null

    //Liaison avec le fragement
    init {
        fullNameTextView=itemView.findViewById(R.id.fullNameTextView)
        descriptionTextView=itemView.findViewById(R.id.descriptionTextView)
    }

    // fait le lien entre un objet item et l'adapter
    //Affiche les éléments dans le fragement user_item
    fun bind(item: InfoItem, action: OnItemClickListener){
        fullNameTextView?.text = "${item.name}"
        descriptionTextView?.text = " ${item.description}"

        itemView.setOnClickListener{
             action.onItemClick(item,adapterPosition)
        }

    }

}

