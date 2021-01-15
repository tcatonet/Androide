package projet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.androidproject.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_edit_item.*

class EditItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)


        back?.setOnClickListener {
            val intent = Intent(this, ListeItemsActivity::class.java)
            startActivity(intent)
        }


        validate?.setOnClickListener {
            val gson = Gson()
            val intent = Intent(this, ListeItemsActivity::class.java)

            var name = name.text.toString()
            var description = description.text.toString()
            var isUnique = true

            val gson_charge = Gson()

            val sharedPreference =getSharedPreferences("projet", MODE_PRIVATE)
            val json :String? = sharedPreference.getString("myList_items",null)
            Log.d("error", "validate")

            if (json != null){

                val myListItems = gson_charge.fromJson(json,  object : TypeToken<MutableList<InfoItem>>() {}.type) as MutableList<InfoItem>

                // Teste si le nom saisie à la création de l'item est unique
                for (item in myListItems){
                    if(item.name == name){
                        isUnique = false
                        break
                    }
                }
                if (isUnique) {
                    val item = InfoItem(name, description)
                    intent.putExtra("identifier", gson.toJson(item))
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "Le nom doit être unique" , Toast.LENGTH_SHORT).show()

                }

            }else{
                val item = InfoItem(name, description)
                intent.putExtra("identifier", gson.toJson(item))
                startActivity(intent)
            }
        }

    }
}

