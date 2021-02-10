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

        val sharedPreference =getSharedPreferences("projet", MODE_PRIVATE)

        modifName.setText(sharedPreference.getString("name",null))
        modifDescription.setText(sharedPreference.getString("description",null))

        val nameOrigin = sharedPreference.getString("name",null)
        val decriptionOrigin = sharedPreference.getString("description",null)
        val adresse = sharedPreference.getString("adresse",null)

        //Click sur le bouton retour
        back?.setOnClickListener {
            val intent = Intent(this, ViewItemActivity::class.java)

            startActivity(intent)
        }

        //Click sur le bouton valider
        validate?.setOnClickListener {
            val intent = Intent(this, ViewItemActivity::class.java)

            var name = modifName.text.toString()
            var description = modifDescription.text.toString()


            val list_items = dbHelper.getAllItem()
            var isUnique = true

            if( name.trim() != "") {
                // Teste si le nom saisie à la modifiction de l'item est unique
                if (name != nameOrigin) {
                    for (item in list_items) {
                        if (name.trim() == item.name.trim()) {
                            isUnique = false
                        }
                    }
                }
                if (isUnique) {

                    for (item in list_items) {

                        if (item.name.trim() == nameOrigin) {
                            if (adresse != null) {
                                dbHelper.updateData(nameOrigin, name, description)
                            }
                            break
                        }
                    }
                    Toast.makeText(this, "Item modifié", Toast.LENGTH_SHORT).show()

                    sharedPreference.edit().putString("name",  modifName.getText().toString() as String?).apply()
                    sharedPreference.edit().putString("description",modifDescription.getText().toString() as String?).apply()
                    startActivity(intent)

                } else {
                    Toast.makeText(this, "Le nom doit être unique", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Le nom ne peut pas être vide", Toast.LENGTH_SHORT).show()

            }
        }
    }


}