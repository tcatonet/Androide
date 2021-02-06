package View

import Modele.DataBaseHelper
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.androidproject.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_edit_item.*
import java.io.IOException
import java.util.*
import kotlin.concurrent.schedule

class EditItemActivity : AppCompatActivity() {

    internal var dbHelper = DataBaseHelper(this)
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var map: GoogleMap
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    val FINE_LOCATION_RQ =101
     val FINE_COARSE_LOCATION = 102


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
                        if(c.toString().equals('"'.toString(), true) ){

                        }else{
                            addressText +=c
                        }
                    }
                }
            }
        } catch (e: IOException) {
        }

        return addressText
    }


    private fun requestLocationUpdates() {
        val request = LocationRequest()
        request.setFastestInterval(100)
                .setInterval(200).priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        var client = LocationServices.getFusedLocationProviderClient(this)
        val permission = intArrayOf(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION))
        if (permission[0] == PackageManager.PERMISSION_GRANTED) {
            val location = arrayOf<Location?>(Location(LocationManager.GPS_PROVIDER))
            var locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    location[0] = locationResult.lastLocation
                }
            }
            client.requestLocationUpdates(request, locationCallback, null)
        }
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





            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                val builder = AlertDialog.Builder(this)
                builder.apply {
                    setTitle("Permission")
                    setMessage("Allow localization")
                    setPositiveButton("OK") { dialog, wich ->
                        ActivityCompat.requestPermissions(this@EditItemActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
                        ActivityCompat.requestPermissions(this@EditItemActivity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)

                    }
                }
                builder.show()
            } else {

            }




            val manager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (isNetworkConnected()){
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->

                        var cpt = 0
                        while (location == null && cpt < 1) {
                            cpt = cpt + 1
                            requestLocationUpdates()
                        }

                        if (location != null) {


                            lastLocation = location
                            this.latitude = location.latitude
                            this.longitude = location.longitude

                            var adresse = getAddress(this.latitude, this.longitude)
                            var latitude = this.latitude.toString()
                            var longitude = this.longitude.toString()

                            var isUnique = true
                            val res = dbHelper.getAllItem()

                            //Si la liste contient des items et que le nom du nouvel item  a été modifié, on s'assure que ce nouveau nom n'est pas déja associé à un item de la liste
                            if (res != null) {

                                // Teste si le nom saisie à la création de l'item est unique
                                for (item in res) {
                                    if (item.name.trim() == name.trim()) {
                                        isUnique = false
                                        break
                                    }
                                }
                                if (isUnique) {
                                    if(adresse != "" && latitude != "" && longitude != "") {
                                        dbHelper.insertData(name, adresse, description, latitude, longitude)
                                        Toast.makeText(this, "Item ajouté", Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(this, "Impossible de trouver votre position", Toast.LENGTH_SHORT).show()
                                    }

                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this, "Le nom doit être unique", Toast.LENGTH_SHORT).show()

                                }

                            } else {
                                dbHelper.insertData(name, description, adresse, latitude, longitude)
                                startActivity(intent)
                            }


                        }
                    }


                } else {
                    Toast.makeText(this, "La géolocalisation n'est pas activé", Toast.LENGTH_SHORT).show()
                }
        }else{
                Toast.makeText(this, "Vous n'êtes pas connecté à internet", Toast.LENGTH_SHORT).show()
            }

        }

    }


    private fun isNetworkConnected(): Boolean
    {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            val activeNetwork =  connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)
            networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        }
        else
        {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
            return if (connectivityManager is ConnectivityManager) {
                val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
                networkInfo?.isConnected ?: false
            } else false
        }
    }


    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
}

