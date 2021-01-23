package WebServices

import Modele.InfoItem
import retrofit2.Call
import retrofit2.http.GET

interface  NetworkInterface {

    @GET("json/get/E1j-LcHAK?delay=2000/")
    fun retrieveItem(): Call<MutableList<InfoItem>>

}