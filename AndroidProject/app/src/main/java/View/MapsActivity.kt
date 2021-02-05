package View

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.example.androidproject.R
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

class MapsActivity : FragmentActivity(), OnMapReadyCallback {

    internal lateinit var mLocationRequest: LocationRequest

    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101
    private lateinit var myMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(this@MapsActivity)


        fetchLocation()

        mLocationRequest = LocationRequest()

    }



    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), permissionCode)

            return
        }

        Toast.makeText(this, "YES" , Toast.LENGTH_SHORT).show()

        val task = fusedLocationProviderClient.lastLocation

        task.addOnSuccessListener { location ->
            if (location != null) {

                currentLocation = location

                val supportMapFragment = (supportFragmentManager.findFragmentById(R.id.map) as
                        SupportMapFragment?)!!
                supportMapFragment.getMapAsync(this@MapsActivity)
            }
        }
    }
    override fun onMapReady(googleMap: GoogleMap?) {

        val sydney = LatLng(-33.852, 151.211)
        googleMap!!.addMarker(MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney"))

        /*val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
        val markerOptions = MarkerOptions().position(latLng).title("I am here!")
        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f))
        googleMap?.addMarker(markerOptions)*/
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
                                            grantResults: IntArray) {
        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }

        }





    }






}