package WebServices

import View.LoaderActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object API {

    private var nentworkIt: NetworkInterface? = null
    // build de l'interface
    init{
        val retrofit  = Retrofit.Builder()
            .baseUrl("http://34.230.81.125:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        nentworkIt = retrofit.create(NetworkInterface::class.java)

    }

    // lance un appel
    fun retrieveItem(callback: LoaderActivity)
    {
        val call = nentworkIt?.retrieveItem()
        call?.enqueue(callback)
    }
}