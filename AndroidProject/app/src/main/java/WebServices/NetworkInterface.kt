package WebServices

import Modele.InfoItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface  NetworkInterface {
    val test: Int

    @GET("/get/all")
    fun retrieveItem(): Call<MutableList<InfoItem>>

    @GET("/del/{name}")
    fun deleteItem(@Path("name") name: String): Call<MutableList<InfoItem>>

    @GET("/add/{item}")
    fun addItem(@Path("/add/{item}") item: String): Call<MutableList<InfoItem>>

}