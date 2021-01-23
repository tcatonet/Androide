package View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.androidproject.R
import kotlinx.android.synthetic.main.activity_edit_item.back
import kotlinx.android.synthetic.main.activity_view_item.*
import Modele.DataBaseHelper
import android.content.Context
import android.location.LocationManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_liste_items.*

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
            val intent = Intent(this, ListeItemsActivity::class.java)
            var name = name.text.toString()
            val list_items = dbHelper.getAllItem()

            //On parcours tous les items pour trouver celui à supprimer
            for (item in list_items){
                if(item.name == name){
                    dbHelper.deleteOneItem(name)
                    startActivity(intent)
                    break
                }
            }

        }

        mapButton?.setOnClickListener {
            val manager = getSystemService( Context.LOCATION_SERVICE ) as LocationManager

            if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                Toast.makeText(this, "La géolocalisation n'est pas activée", Toast.LENGTH_SHORT).show()
            }else{
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }

        }





    }
}