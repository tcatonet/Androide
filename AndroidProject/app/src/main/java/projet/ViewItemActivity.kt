package projet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.androidproject.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_edit_item.*
import kotlinx.android.synthetic.main.activity_edit_item.back
import kotlinx.android.synthetic.main.activity_modif_item.*
import kotlinx.android.synthetic.main.activity_view_item.*

class ViewItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_item)


        //On récupère les valeur des champ name et description
        val name: TextView = findViewById(R.id.name) as TextView
        val description: TextView = findViewById(R.id.description) as TextView

        name.text = getIntent().getStringExtra("name")
        description.text = getIntent().getStringExtra("description")


        back?.setOnClickListener {
            val intent = Intent(this, ListeItemsActivity::class.java)
            startActivity(intent)
        }

        edit?.setOnClickListener {
            val intent = Intent(this, ModifItemActivity::class.java)
            intent.putExtra("name", name.text)
            intent.putExtra("description",description.text)
            startActivity(intent)
        }



        delete?.setOnClickListener {
            val intent = Intent(this, ListeItemsActivity::class.java)
            var name = name.text.toString()
            val gson_charge = Gson()
            val sharedPreference =getSharedPreferences("projet", MODE_PRIVATE)
            val json : String? = sharedPreference.getString("myList_items",null)

            if (json != null) {
                Log.d("delete",json)
            }else {
                Log.d("delete","null")

            }

            val list_items = gson_charge.fromJson(json,  object : TypeToken<MutableList<InfoItem>>() {}.type) as MutableList<InfoItem>

            // Teste si le nom saisie à la création de l'item est unique
            for (item in list_items){
                if(item.name == name){

                    list_items.remove(item)

                    sharedPreference.edit().putString("myList_items",gson_charge.toJson(list_items)).apply()
                    startActivity(intent)
                    break
                }
            }

            //startActivity(intent)

        }


    }
}