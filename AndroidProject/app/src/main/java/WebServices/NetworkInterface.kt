package WebServices

import Modele.InfoItem
import Modele.InfoItemReception
import Modele.InfoItemReceptionSupression
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface  NetworkInterface {


    @GET("/getall")
    fun retrieveItem(): Call<MutableList<InfoItemReception>>

    @GET("/del/{name}")
    fun deleteItem(@Path("name") name: String): Call<InfoItemReceptionSupression>

    @GET("/add/{item}")
    fun addItem(@Path("item") item: String): Call<InfoItemReception>

}