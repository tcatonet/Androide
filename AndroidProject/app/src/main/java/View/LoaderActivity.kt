package View

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidproject.R
import Modele.DataBaseHelper
import Modele.InfoItem
import WebServices.API
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoaderActivity : AppCompatActivity(), Callback<MutableList<InfoItem>> {


    internal var dbHelper = DataBaseHelper(this)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loader)
        var intent = Intent(this, ListeItemsActivity::class.java)
        var resData = dbHelper.allData

        //Si la base de donnée local est vide et que le réseau est disponible, on lance un appel à l'api pour charger une liste
        if (resData.count==0) {
            if(isNetworkConnected()){

                API.retrieveItem(this)


            }else{ // Si la liste est vide et qu'il n'y a pas de réseau, on redirige l'utilisateur vers ListeItemsActivity sans charger de liste

                intent = Intent(this, NoNetworkActivity::class.java)
                startActivity(intent)
            }

        }
        else{ // Si la liste n'est pas vide, on redirige l'utilisateur vers ListeItemsActivity
            startActivity(intent)
        }

    }




    //Test si le réseau est  disponible

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

    override fun onResponse(call: Call<MutableList<InfoItem>>, response: Response<MutableList<InfoItem>>) {

        Log.d("appel", response.body().toString())
        val listItem = response.body()

        // On charge les données de l'api en BD
        if (listItem != null) {
            for (item in listItem) {
                dbHelper.insertData(item.name, item.description, item.adresse, item.latittude,item.longitude)
            }
        }
        intent = Intent(this, ListeItemsActivity::class.java)
        startActivity(intent)
    }

     override fun onFailure(call: Call<MutableList<InfoItem>>, t: Throwable) {

         intent = Intent(this, NoNetworkActivity::class.java)
         startActivity(intent)
      }


}