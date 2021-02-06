package Modele

data class InfoItem(var name: String,var adresse: String, var description: String, var longitude : String, var latitude : String)

data class InfoItemReception(var id: Int,var name: String,var adresse: String, var description: String, var longitude : String, var latitude : String)
data class InfoItemReceptionSupression(var name: String, var deleted : Boolean)