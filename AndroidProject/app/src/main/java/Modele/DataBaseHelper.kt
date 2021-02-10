package Modele

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DataBaseHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME , null, 1){



    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_NAME (ID INTEGER PRIMARY KEY AUTOINCREMENT, $COL_NAME TEXT, $COL_DESCRIPTION TEXT, $COL_ADRESSE TEXT, $COL_LATITUDE TEXT,$COL_LONGITUDE TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
    }

    //Ajoute un nouvel item dans la base de données
    fun insertData(nameVal:String, descriptionVal: String, addressVal: String, longitudeVal: String, latitudeVal: String){
        val db = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(COL_NAME, nameVal)
        contentValue.put(COL_DESCRIPTION, descriptionVal)
        contentValue.put(COL_ADRESSE, addressVal)
        contentValue.put(COL_LATITUDE, latitudeVal)
        contentValue.put(COL_LONGITUDE, longitudeVal)

        db.insert(TABLE_NAME,null,contentValue)
    }

    //Met a jour un item en fonction de son ancien nom, de son nouveau nom et de sa description
    fun updateData(lastName:String,newName:String, descriptionVal: String){
        val db = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(COL_NAME, newName)
        contentValue.put(COL_DESCRIPTION, descriptionVal)

        db.update(TABLE_NAME, contentValue,"NAME = ?", arrayOf(lastName))
    }

    // Suprime un item dont le nom est passé en paramètre
    fun deleteOneItem(name: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME,"NAME = ?", arrayOf(name))
    }

    //Retourne une MutableList des item en BD
    fun getAllItem(): MutableList<InfoItem> {
        val db = this.writableDatabase
        val res = db.rawQuery("SELECT name,description,adresse,latittude,longitude FROM $TABLE_NAME "  , null )
        val itemList: MutableList<InfoItem> = mutableListOf()

        res.moveToFirst()
        while(!res.isAfterLast) {
            var name = res.getString(res.getColumnIndex(COL_NAME))
            var description= res.getString(res.getColumnIndex(COL_DESCRIPTION))
            var adresse = res.getString(res.getColumnIndex(COL_ADRESSE))
            var latitude= res.getString(res.getColumnIndex(COL_LATITUDE))
            var longitude= res.getString(res.getColumnIndex(COL_LONGITUDE))

            itemList.add(InfoItem(name,adresse,description, latitude,longitude))
            res.moveToNext()
        }
        return itemList
    }

    val allData : Cursor
        get(){
            val db = this.writableDatabase
            val res = db.rawQuery("SELECT * FROM $TABLE_NAME", null )
            return res
        }

    companion object{
        const val DATABASE_NAME="DBListe"
        const val TABLE_NAME="ListeItem"
        const val COL_NAME="name"
        const val COL_DESCRIPTION="description"
        const val COL_ADRESSE="adresse"
        const val COL_LONGITUDE="longitude"
        const val COL_LATITUDE="latittude"


    }
}