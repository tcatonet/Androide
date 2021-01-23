package Modele

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME , null, 1){



    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_NAME (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, DESCRIPTION TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        }

    }

    //Ajoute un nouvel item dans la base de données
    fun insertData(nameVal:String, descriptionVal: String){
        val db = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(COL_NAME, nameVal)
        contentValue.put(COL_DESCRIPTION, descriptionVal)
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
        val res = db.rawQuery("SELECT name,description FROM $TABLE_NAME "  , null )
        val itemList: MutableList<InfoItem> = mutableListOf()

        res.moveToFirst()
        while(!res.isAfterLast) {
            var name = res.getString(res.getColumnIndex("NAME"))
            var description= res.getString(res.getColumnIndex("DESCRIPTION"))

            itemList.add(InfoItem(name,description))
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
        const val COL_ID="id"
        const val COL_NAME="name"
        const val COL_DESCRIPTION="description"

    }
}