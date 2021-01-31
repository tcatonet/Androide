package WebServices

import Modele.InfoItem
import retrofit2.Call
import retrofit2.http.GET

interface  NetworkInterface {

    @GET("/")
    fun retrieveItem(): Call<MutableList<InfoItem>>

}