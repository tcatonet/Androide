package View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.androidproject.R
import kotlinx.android.synthetic.main.activity_edit_item.back
import kotlinx.android.synthetic.main.activity_view_item.*
import Modele.DataBaseHelper
import android.location.LocationManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class ViewItemActivity : AppCompatActivity() {

    internal var dbHelper = DataBaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_item)


        //On récupère les valeur des champ name et description
        val name: TextView = findViewById(R.id.name)
        val description: TextView = findViewById<TextView>(R.id.description)

        name.text = getIntent().getStringExtra("name")
        description.text = getIntent().getStringExtra("description")

        //Click sur le bouton retour
        back?.setOnClickListener {
            val intent = Intent(this, ListeItemsActivity::class.java)
            startActivity(intent)
        }

        //Click sur le bouton éditer
        edit?.setOnClickListener {
            val intent = Intent(this, ModifItemActivity::class.java)
            intent.putExtra("name", name.text)
            intent.putExtra("description",description.text)
            startActivity(intent)
        }


        //Click sur le bouton supprimer
        delete?.setOnClickListener {


            val builder = AlertDialog.Builder(this)
            builder.setTitle("Supression")
            builder.setMessage("Voulez-vous vraiment supprimer l'item " + name.text)

            builder.setPositiveButton("Oui"){dialogInterface, which ->
                val intent = Intent(this, ListeItemsActivity::class.java)
                var name = name.text.toString()
                val list_items = dbHelper.getAllItem()

                //On parcours tous les items pour trouver celui à supprimer
                for (item in list_items){
                    if(item.name == name){
                        dbHelper.deleteOneItem(name)
                        Toast.makeText(this, "Item supprimé" , Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        break
                    }
                }
            }

            builder.setNegativeButton("Non"){dialogInterface, which ->

            }

            builder.show()

        }


        mapButton?.setOnClickListener {

            val manager = getSystemService( LOCATION_SERVICE ) as LocationManager

            if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                Toast.makeText(this, "La géolocalisation n'est pas activée", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }

        }





    }
}