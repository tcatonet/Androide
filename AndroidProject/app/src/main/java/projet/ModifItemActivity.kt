package projet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

    internal var dbHelper = DataBaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modif_item)

        modifName.setText(getIntent().getStringExtra("name"))
        modifDescription.setText(getIntent().getStringExtra("description"))
        val nameOrigin = getIntent().getStringExtra("name")
        val decriptionOrigin = getIntent().getStringExtra("description")

        //Click sur le bouton retour
        back?.setOnClickListener {
            val intent = Intent(this, ViewItemActivity::class.java)
            intent.putExtra("name", nameOrigin)
            intent.putExtra("description",decriptionOrigin)
            startActivity(intent)
        }

        //Click sur le bouton valider
        validate?.setOnClickListener {
            val intent = Intent(this, ViewItemActivity::class.java)

            var name = modifName.text.toString()
            var description = modifDescription.text.toString()



            val list_items = dbHelper.getAllItem()
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
                        dbHelper.updateData(nameOrigin, name, description)
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