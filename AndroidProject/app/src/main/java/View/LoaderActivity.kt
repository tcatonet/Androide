package View

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.androidproject.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import Modele.DataBaseHelper
import Modele.InfoItem
import java.net.URL

class LoaderActivity : AppCompatActivity() {


    internal var dbHelper = DataBaseHelper(this)

    //Test si le réseau est  disponible
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)
        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loader)
        var intent = Intent(this, ListeItemsActivity::class.java)
        var resData = dbHelper.allData

        //Si la base de donnée local est vide et que le réseau est disponible, on lance un appel à l'api pour charger une liste
        if (resData.count==0) {
            if(isNetworkAvailable()){
                doAsync {
                    val url = URL("https://next.json-generator.com/api/json/get/E1j-LcHAK?delay=2000")
                    val stringResponse = url.readText()
                    val initListe = Gson().fromJson( stringResponse,object : TypeToken<MutableList<InfoItem>>() {}.type) as MutableList<InfoItem>

                    // On charge les données de l'api en BD
                    for (item in initListe) {
                        dbHelper.insertData(item.name,item.description)
                    }

                    uiThread {
                        startActivity(intent)

                    }
                }
            }else{ // Si la liste est vide et qu'il n'y a pas de réseau, on redirige l'utilisateur vers ListeItemsActivity sans charger de liste
                intent = Intent(this, NoNetworkActivity::class.java)
                startActivity(intent)
            }

        }
        else{ // Si la liste n'est pas vide, on redirige l'utilisateur vers ListeItemsActivity
            startActivity(intent)
        }

    }
}