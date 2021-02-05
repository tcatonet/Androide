package View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.androidproject.R
import kotlinx.android.synthetic.main.activity_edit_item.*
import Modele.DataBaseHelper
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import java.io.IOException

class EditItemActivity : AppCompatActivity() {

    internal var dbHelper = DataBaseHelper(this)
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var map: GoogleMap
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

        }
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                this.latitude = location.latitude
                this.longitude = location.longitude
                Toast.makeText(this, "this.latitude" + this.latitude , Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "this.longitude" + this.longitude , Toast.LENGTH_SHORT).show()

                val currentLatLng = LatLng(location.latitude, location.longitude)
            }

        }
    }

    private fun getAddress(latitude_: Double, longitude_: Double): String {
        // 1
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""

        try {

            addresses = geocoder.getFromLocation(latitude_, longitude_, 1)

            if (null != addresses && !addresses.isEmpty()) {

                address = addresses[0]
                var cpt = 0
                for (c in address.toString()) {

                    if (c == '"') {
                        cpt += 1
                    }
                    if (cpt == 1) {
                        if(c.toString().equals('"'.toString(),true) ){

                        }else{
                            addressText +=c
                        }
                    }
                }

            }
        } catch (e: IOException) {
            Log.e("MapsActivity", e.localizedMessage)
        }

        return addressText
    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        //Click sur le bouton retour
        back?.setOnClickListener {
            val intent = Intent(this, ListeItemsActivity::class.java)
            startActivity(intent)
        }

        //Click sur le bouton valider pur ajuoter un item
        validate?.setOnClickListener {
            val intent = Intent(this, ListeItemsActivity::class.java)

            var name = name.text.toString()
            var description = description.text.toString()

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

            }
            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location
                    this.latitude = location.latitude
                    this.longitude = location.longitude
                    Toast.makeText(this, "this.latitude" + this.latitude , Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "this.longitude" + this.longitude , Toast.LENGTH_SHORT).show()

                    val currentLatLng = LatLng(location.latitude, location.longitude)


                    var adresse = getAddress(this.latitude, this.longitude)
                    var latitude = this.latitude.toString()
                    var longitude = this.longitude.toString()

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
                            dbHelper.insertData(name,adresse, description,latitude,longitude)
                            Toast.makeText(this, "Item ajouté" , Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        }else{
                            Toast.makeText(this, "Le nom doit être unique" , Toast.LENGTH_SHORT).show()

                        }

                    }else{
                        dbHelper.insertData(name, description,adresse,latitude,longitude)
                        startActivity(intent)
                    }

                }

            }









        }

    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}

