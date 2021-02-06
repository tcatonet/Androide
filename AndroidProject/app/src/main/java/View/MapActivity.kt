package View

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.androidproject.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class MapActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMarkerClickListener  {

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    private fun setUpMap(latitude : Double,longitude:Double) {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )

            return
        }


        map.isMyLocationEnabled = true

            fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
                if (location != null) {
                    lastLocation = location
                    val currentLatLng = LatLng(latitude, longitude)
                    placeMarkerOnMap(currentLatLng)
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                }

        }
    }


    private fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions().position(location)
        val titleStr = getIntent().getStringExtra("adresse")

        markerOptions.title(titleStr)
        map.addMarker(markerOptions)
    }



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

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



    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)

        val latitude = getIntent().getStringExtra("latitude").toString().toDouble()
        val longitude = getIntent().getStringExtra("longitude").toString().toDouble()

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            val builder = AlertDialog.Builder(this)
            builder.apply {
                setTitle("Permission")
                setMessage("Autoriser la localisation")
                setPositiveButton("OK") { dialog, wich ->
                    ActivityCompat.requestPermissions(this@MapActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), EditItemActivity.LOCATION_PERMISSION_REQUEST_CODE)
                    ActivityCompat.requestPermissions(this@MapActivity, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION), EditItemActivity.LOCATION_PERMISSION_REQUEST_CODE)

                }
            }
            builder.show()
        }

        setUpMap(latitude,longitude)


        map.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            var cpt = 0
            while(location == null && cpt < 20) {
                cpt=cpt+1
                requestLocationUpdates()
            }

            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(latitude, longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 8f))
            }else{
                Toast.makeText(this, "Localisation impossible. Réssayez.", Toast.LENGTH_SHORT).show()

            }
        }


    }

    override fun onMarkerClick(p0: Marker?) = false



    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

}