package View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.androidproject.R
import kotlinx.android.synthetic.main.activity_edit_item.back
import kotlinx.android.synthetic.main.activity_edit_item.validate
import kotlinx.android.synthetic.main.activity_modif_item.*
import Modele.DataBaseHelper

class ModifItemActivity : AppCompatActivity() {

    internal var dbHelper = DataBaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modif_item)

        modifName.setText(getIntent().getStringExtra("name"))
        modifDescription.setText(getIntent().getStringExtra("description"))

        val nameOrigin = getIntent().getStringExtra("name")
        val decriptionOrigin = getIntent().getStringExtra("description")
        val adresse = getIntent().getStringExtra("adresse")

        //Click sur le bouton retour
        back?.setOnClickListener {
            val intent = Intent(this, ViewItemActivity::class.java)
            intent.putExtra("name", nameOrigin)
            intent.putExtra("description",decriptionOrigin)
            intent.putExtra("adresse",adresse)

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
                    if(name.trim() == item.name.trim()){
                        isUnique = false
                    }
                }
            }
            if(isUnique) {

                for (item in list_items) {

                    if (item.name.trim() == nameOrigin) {
                        dbHelper.updateData(nameOrigin, name, description)
                        break
                    }
                }
                Toast.makeText(this,"Item modifié", Toast.LENGTH_SHORT).show()
                intent.putExtra("name", modifName.getText().toString())
                intent.putExtra("description",modifDescription.getText().toString())
                intent.putExtra("adresse",adresse)

                startActivity(intent)

            }else{
                Toast.makeText(this, "Le nom doit être unique" , Toast.LENGTH_SHORT).show()
            }
        }
    }


}