package View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.androidproject.R
import kotlinx.android.synthetic.main.activity_edit_item.*
import Modele.DataBaseHelper

class EditItemActivity : AppCompatActivity() {

    internal var dbHelper = DataBaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        //Click sur le bouton retour
        back?.setOnClickListener {
            val intent = Intent(this, ListeItemsActivity::class.java)
            startActivity(intent)
        }

        //Click sur le bouton valider
        validate?.setOnClickListener {
            val intent = Intent(this, ListeItemsActivity::class.java)

            var name = name.text.toString()
            var description = description.text.toString()
            var isUnique = true
            val res = dbHelper.getAllItem()

            //Si la liste contient des items et que le nom du nouvel item  a été modifié, on s'assure que ce nouveau nom n'est pas déja associé à un item de la liste
            if (res != null){

                // Teste si le nom saisie à la création de l'item est unique
                for (item in res){
                    if(item.name == name){
                        isUnique = false
                        break
                    }
                }
                if (isUnique) {
                    dbHelper.insertData(name, description)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "Le nom doit être unique" , Toast.LENGTH_SHORT).show()

                }

            }else{
                dbHelper.insertData(name, description)
                startActivity(intent)
            }
        }

    }
}

