package projet


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R


class ItemViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
    RecyclerView.ViewHolder(
        inflater.inflate(R.layout.user_item, parent, false)
    ) {

    private var fullNameTextView: TextView? = null
    private var descriptionTextView: TextView? = null

    init {
        fullNameTextView=itemView.findViewById(R.id.fullNameTextView)
        descriptionTextView=itemView.findViewById(R.id.descriptionTextView)
    }

    fun bind(item: InfoItem, action:OnItemClickListener){
        fullNameTextView?.text = "${item.name}"
        descriptionTextView?.text = " ${item.description}"

        itemView.setOnClickListener{
             action.onItemClick(item,adapterPosition)
        }

    }

}

