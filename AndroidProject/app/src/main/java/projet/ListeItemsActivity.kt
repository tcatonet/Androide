package projet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidproject.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_liste_items.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL

class ListeItemsActivity : AppCompatActivity(), OnItemClickListener  {
//v1.000
    private var list_items = mutableListOf(
        InfoItem("none", "none"),
    )
    internal var dbHelper = DataBaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        //On charge la liste d'item sur la vue
        val sharedPreference =getSharedPreferences("projet", MODE_PRIVATE)
        val gson_charge = Gson()
        var resData = dbHelper.allData

        val memoryListjson:String? = sharedPreference.getString("myList_items",null)

        if(resData.count==0){
            Log.d("error", "NULL")
            this.list_items = ArrayList()
        }else{
            Log.d("error", "Pas NULL")
            this.list_items = gson_charge.fromJson(memoryListjson,  object : TypeToken<MutableList<InfoItem>>() {}.type)
            //for (item in resData) {
             //   this.list_items.add( InfoItem(item.name,item.description))
            //}
        }


        //Création de la vue
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_liste_items)
        recyclerView.addItemDecoration(DividerItemDecoration(this,1))
        recyclerView.layoutManager = LinearLayoutManager(this@ListeItemsActivity)
        recyclerView.adapter = ItemAdapter(list_items, this)


        //On ajoute un nouvelle item à la liste
        val gson_save = Gson()
        var yourObject: InfoItem? = gson_save.fromJson<InfoItem>(intent.getStringExtra("identifier"), InfoItem::class.java)

        if (yourObject != null) {

            //Empeche l'ajout d'un item à chaque rotation d'écran
            val destroy :String? = null
            intent.putExtra("identifier", destroy)

            this.list_items.add(yourObject)
            val gson = Gson()
            sharedPreference.edit().putString("myList_items",gson.toJson(list_items)).apply()
            yourObject=null
        }

        //On click sur le bouton addItem
        addItem?.setOnClickListener {
            val intent = Intent(this, EditItemActivity::class.java)
            startActivity(intent)

        }
    }

    override fun onItemClick(item: InfoItem, position: Int) {
        val intent = Intent(this, ViewItemActivity::class.java)
        intent.putExtra("name", item.name)
        intent.putExtra("description", item.description)
        startActivity(intent)
    }



}