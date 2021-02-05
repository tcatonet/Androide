package WebServices

import Modele.InfoItem
import retrofit2.Call
import retrofit2.http.GET

interface  NetworkInterface {
    val test: Int

    @GET("/"+"all")
    fun retrieveItem(): Call<MutableList<InfoItem>>

}