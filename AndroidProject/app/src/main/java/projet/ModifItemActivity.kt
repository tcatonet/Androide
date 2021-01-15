package projet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.androidproject.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_edit_item.*
import kotlinx.android.synthetic.main.activity_edit_item.back
import kotlinx.android.synthetic.main.activity_edit_item.validate
import kotlinx.android.synthetic.main.activity_modif_item.*
import kotlinx.android.synthetic.main.activity_view_item.*

class ModifItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modif_item)

        modifName.setText(getIntent().getStringExtra("name"))
        modifDescription.setText(getIntent().getStringExtra("description"))
        val nameOrigin = getIntent().getStringExtra("name")
        val decriptionOrigin = getIntent().getStringExtra("description")


        back?.setOnClickListener {
            val intent = Intent(this, ViewItemActivity::class.java)
            intent.putExtra("name", nameOrigin)
            //Toast.makeText(this, nameOrigin , Toast.LENGTH_SHORT).show()

            intent.putExtra("description",decriptionOrigin)
            startActivity(intent)
        }

        validate?.setOnClickListener {
            val intent = Intent(this, ViewItemActivity::class.java)

            var name = modifName.text.toString()
            var description = modifDescription.text.toString()

            val gson_change = Gson()
            val sharedPreference =getSharedPreferences("projet", MODE_PRIVATE)
            val json :String? = sharedPreference.getString("myList_items",null)
            val list_items = gson_change.fromJson(json,  object : TypeToken<MutableList<InfoItem>>() {}.type) as MutableList<InfoItem>
            var isUnique = true

            // Teste si le nom saisie à la modifiction de l'item est unique
            if(name != nameOrigin){
                for (item in list_items) {
                    if(name == item.name){
                        isUnique = false
                    }
                }
            }

            if(isUnique) {
                for (item in list_items) {
                    if (item.name == nameOrigin) {
                        item.name = name
                        item.description = description

                        val sharedPreference = getSharedPreferences("projet", MODE_PRIVATE)
                        sharedPreference.edit().putString("myList_items", gson_change.toJson(list_items)).apply()
                        break
                    }
                }

                intent.putExtra("name", modifName.getText().toString())
                intent.putExtra("description",modifDescription.getText().toString())
                startActivity(intent)

            }else{
                Toast.makeText(this, "Le nom doit être unique" , Toast.LENGTH_SHORT).show()
            }



        }

    }


}