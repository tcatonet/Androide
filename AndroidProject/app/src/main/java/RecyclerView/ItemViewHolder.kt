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
    private var adresseTextView: TextView? = null

    //Liaison avec le fragement
    init {
        fullNameTextView=itemView.findViewById(R.id.fullNameTextView)
        adresseTextView=itemView.findViewById(R.id.adresseTextView)

    }

    // fait le lien entre un objet item et l'adapter
    //Affiche les éléments dans le fragement user_item
    fun bind(item: InfoItem, action: OnItemClickListener){
        fullNameTextView?.text = "${item.name}"
        adresseTextView?.text = " ${item.adresse}"

        itemView.setOnClickListener{
             action.onItemClick(item,adapterPosition)
        }

    }

}

