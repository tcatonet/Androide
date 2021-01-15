package projet

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DataBaseHelper (context: Context) : SQLiteOpenHelper(context,DATABASE_NAME , null, 1){



    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_NAME (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, DESCRIPTION TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        }

    }

    fun insertData(nameVal:String, descriptionVal: String){
        val db = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(COL_NAME, nameVal)
        contentValue.put(COL_DESCRIPTION, descriptionVal)
        db.insert(TABLE_NAME,null,contentValue)
    }

    fun updateData(id: String, nameVal:String, descriptionVal: String){
            val db = this.writableDatabase
            val contentValue = ContentValues()
            contentValue.put(COL_ID, id)
            contentValue.put(COL_NAME, nameVal)
            contentValue.put(COL_DESCRIPTION, descriptionVal)
            db.update(COL_ID, contentValue,"ID = ?", arrayOf(id) )
    }

    fun deleteData(id: String){
        val db = this.writableDatabase
        db.delete(TABLE_NAME,"ID = ?", arrayOf(id))
    }

    fun getOneItem(id: Int): InfoItem {
        val db = this.writableDatabase
        val res = db.rawQuery("SELECT name,description FROM $TABLE_NAME WHERE id = $id"  , null )

        res.moveToFirst()
        if(!res.isAfterLast) {
            var name = res.getString(res.getColumnIndex("NAME"))
            var description= res.getString(res.getColumnIndex("DESCRIPTION"))
            return InfoItem(name,description)
        }
        return InfoItem("none","none")
    }


    fun deleteOneItem(name: String) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME,"NAME = ?", arrayOf(name))
    }

    fun getAllItem(): MutableList<InfoItem> {
        val db = this.writableDatabase
        val res = db.rawQuery("SELECT name,description FROM $TABLE_NAME "  , null )

        val itemList: MutableList<InfoItem> = mutableListOf()
        res.moveToFirst()

        while(!res.isAfterLast) {
            Log.d("error", "getAllItem")

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