package View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidproject.R
import kotlinx.android.synthetic.main.activity_liste_items.*
import Modele.DataBaseHelper
import Modele.InfoItem
import Modele.ItemAdapter
import Modele.OnItemClickListener
import android.widget.TextView
import androidx.core.view.isVisible


class ListeItemsActivity : AppCompatActivity(), OnItemClickListener {

    private var list_items = mutableListOf<InfoItem>()
    internal var dbHelper = DataBaseHelper(this)



    override fun onCreate(savedInstanceState: Bundle?) {

        //On charge la liste d'item à partie de la BD
        this.list_items = dbHelper.getAllItem()

        val sharedPreference =getSharedPreferences("projet", MODE_PRIVATE)
        sharedPreference.edit().putString("nom", "" as String?).apply()


        //Création de la vue
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liste_items)
        recyclerView.addItemDecoration(DividerItemDecoration(this,1))
        recyclerView.layoutManager = LinearLayoutManager(this@ListeItemsActivity)
        recyclerView.adapter = ItemAdapter(list_items, this)




        val msg: TextView = findViewById(R.id.msg)


        if(this.list_items.size==0){
            msg.isVisible = true
        }


        //On click sur le bouton addItem
        addItem?.setOnClickListener {
            val intent = Intent(this, EditItemActivity::class.java)
            startActivity(intent)

        }




    }

    //On click sur un item de la liste
    override fun onItemClick(item: InfoItem, position: Int) {
        val intent = Intent(this, ViewItemActivity::class.java)

        //On envoit les infos de l'item à ViewItemActivity
        intent.putExtra("name", item.name)
        intent.putExtra("description", item.description)
        intent.putExtra("adresse", item.adresse)
        intent.putExtra("longitude", item.longitude)
        intent.putExtra("latitude", item.latittude)
        startActivity(intent)
    }



}