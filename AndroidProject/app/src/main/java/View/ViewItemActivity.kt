package View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.androidproject.R
import kotlinx.android.synthetic.main.activity_edit_item.back
import kotlinx.android.synthetic.main.activity_view_item.*
import Modele.DataBaseHelper
import Modele.InfoItem
import WebServices.API
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewItemActivity : AppCompatActivity(), Callback<MutableList<InfoItem>> {

    internal var dbHelper = DataBaseHelper(this)


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

        val intent = Intent(this, ViewItemActivity::class.java)
        startActivity(intent)

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_item)

        val name: TextView = findViewById(R.id.name)
        val description: TextView = findViewById<TextView>(R.id.description)
        val adresse: TextView = findViewById<TextView>(R.id.adresse)
        var latitude  = ""
        var longitude = ""

        val sharedPreference =getSharedPreferences("projet", MODE_PRIVATE)

        if(sharedPreference.getString("nom",null) != ""){

            name.text = sharedPreference.getString("nom",null)
            adresse.text = sharedPreference.getString("adresse",null)
            description.text = sharedPreference.getString("description",null)
            latitude = sharedPreference.getString("latitude",null).toString()
            longitude = sharedPreference.getString("longitude",null).toString()

            sharedPreference.edit().putString("nom", "" as String?).apply()

        }else{

            name.text = getIntent().getStringExtra("name")
            adresse.text = getIntent().getStringExtra("adresse")
            description.text = getIntent().getStringExtra("description")
            latitude = getIntent().getStringExtra("latitude").toString()
            longitude = getIntent().getStringExtra("longitude").toString()
        }



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


            val nameUserDelete: String? = name.text as String?

            if (nameUserDelete != null) {
                API.deleteItem(this@ViewItemActivity, nameUserDelete)
            }



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
            requestLocationUpdates()

            val sharedPreference =getSharedPreferences("projet", MODE_PRIVATE)
            sharedPreference.edit().putString("nom", name.text as String?).apply()
            sharedPreference.edit().putString("description",adresse.text as String?).apply()
            sharedPreference.edit().putString("adresse",description.text as String?).apply()
            sharedPreference.edit().putString("longitude",latitude).apply()
            sharedPreference.edit().putString("latitude",longitude).apply()

            val manager = getSystemService( LOCATION_SERVICE ) as LocationManager

            if (isNetworkConnected()){

                if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
                    Toast.makeText(this, "La géolocalisation n'est pas activée", Toast.LENGTH_SHORT).show()
                }else{
                    val intent = Intent(this, MapActivity::class.java)
                    intent.putExtra("adresse", adresse.text)
                    intent.putExtra("latitude", latitude)
                    intent.putExtra("longitude",longitude)
                    startActivity(intent)
                }


            }else{
                Toast.makeText(this, "Vous n'êtes pas connecté à internet", Toast.LENGTH_SHORT).show()
            }


        }






    }

    override fun onResponse(call: Call<MutableList<InfoItem>>, response: Response<MutableList<InfoItem>>) {
        TODO("Not yet implemented")
    }

    override fun onFailure(call: Call<MutableList<InfoItem>>, t: Throwable) {
        TODO("Not yet implemented")
    }
}