package no.kristiania.prg208_1_exam.model.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import no.kristiania.prg208_1_exam.models.DBOriginalImage
import no.kristiania.prg208_1_exam.models.DBResultImage

class DataBaseRepository(
    context: Context,
) : SQLiteOpenHelper(context, "prg208.db", null, 1) {

    companion object {
        const val TABLE_ORIGINAL_IMAGE = "ORIGINAL_IMAGE"
        const val COLUMN_PK_ORIGINAL_ID = "ID"
        const val COLUMN_ORIG_IMAGE_BLOB = "IMAGE_BLOB"
        const val COLUMN_CREATED = "CREATED"

        const val TABLE_RESULT_IMAGE = "RESULT_IMAGE"
        const val COLUMN_PK_RESULT_ID = "ID"
        const val COLUMN_FK_ORIGINAL_IMG_ID = "ORIGINAL_IMG_ID"
        const val COLUMN_STORE_LINK = "STORE_LINK"
        const val COLUMN_NAME = "NAME"
        const val COLUMN_DOMAIN = "DOMAIN"
        const val COLUMN_IDENTIFIER = "IDENTIFIER"
        const val COLUMN_TRACKING_ID = "TRACKING_ID"
        const val COLUMN_THUMBNAIL_LINK = "THUMBNAIL_LINK"
        const val COLUMN_DESCRIPTION = "DESCRIPTION"
        const val COLUMN_RES_IMAGE_LINK = "IMAGE_LINK"
        const val COLUMN_RES_IMAGE_BLOB = "IMAGE_BLOB"
        const val COLUMN_CURRENT_DATE = "CURRENT_DATE"
    }


    // TODO: Check injection safety + close resources

    override fun onCreate(db: SQLiteDatabase?) {
        val createSearchImageTable =
            "CREATE TABLE $TABLE_ORIGINAL_IMAGE " +
                    "($COLUMN_PK_ORIGINAL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_ORIG_IMAGE_BLOB BLOB, " +
                    "$COLUMN_CREATED TEXT)"

        val createResultImageTable =
            "CREATE TABLE $TABLE_RESULT_IMAGE " +
                    "($COLUMN_PK_RESULT_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_STORE_LINK TEXT, " +
                    "$COLUMN_NAME TEXT, " +
                    "$COLUMN_DOMAIN TEXT, " +
                    "$COLUMN_IDENTIFIER TEXT, " +
                    "$COLUMN_TRACKING_ID TEXT, " +
                    "$COLUMN_THUMBNAIL_LINK TEXT, " +
                    "$COLUMN_DESCRIPTION TEXT, " +
                    "$COLUMN_RES_IMAGE_LINK TEXT, " +
                    "$COLUMN_RES_IMAGE_BLOB BLOB, " +
                    "$COLUMN_CURRENT_DATE TEXT, " +
                    "$COLUMN_FK_ORIGINAL_IMG_ID INTEGER, " +
                    "FOREIGN KEY ($COLUMN_FK_ORIGINAL_IMG_ID) REFERENCES $TABLE_ORIGINAL_IMAGE ($COLUMN_PK_ORIGINAL_ID))"

        db?.execSQL(createSearchImageTable)
        db?.execSQL(createResultImageTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_ORIGINAL_IMAGE")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_RESULT_IMAGE")
    }

    fun putOriginalImage(dbOriginalImage: DBOriginalImage): Long {
        val db: SQLiteDatabase = this.writableDatabase
        val originalImageCV = ContentValues()

        if(dbOriginalImage.id != null) {
            originalImageCV.put(COLUMN_PK_ORIGINAL_ID, dbOriginalImage.id)
        }

        originalImageCV.put(COLUMN_ORIG_IMAGE_BLOB, dbOriginalImage.byteArray)
        originalImageCV.put(COLUMN_CREATED, dbOriginalImage.created)

        val origImgId = db.insert(TABLE_ORIGINAL_IMAGE, null, originalImageCV)

        db.close()
        return origImgId
    }

    fun putResultImage(dbResultImage: DBResultImage): Long {
        val resultImageCV = ContentValues()
        val db = this.writableDatabase

        resultImageCV.put(COLUMN_STORE_LINK, dbResultImage.storeLink)
        resultImageCV.put(COLUMN_NAME, dbResultImage.name)
        resultImageCV.put(COLUMN_DOMAIN, dbResultImage.domain)
        resultImageCV.put(COLUMN_IDENTIFIER, dbResultImage.identifier)
        resultImageCV.put(COLUMN_TRACKING_ID, dbResultImage.trackingID)
        resultImageCV.put(COLUMN_THUMBNAIL_LINK, dbResultImage.thumbnailLink)
        resultImageCV.put(COLUMN_DESCRIPTION, dbResultImage.description)
        resultImageCV.put(COLUMN_RES_IMAGE_LINK, dbResultImage.imageLink)
        resultImageCV.put(COLUMN_RES_IMAGE_BLOB, dbResultImage.imageBlob)
        resultImageCV.put(COLUMN_CURRENT_DATE, dbResultImage.currentDate)
        resultImageCV.put(COLUMN_FK_ORIGINAL_IMG_ID, dbResultImage.originalImgID)

        val dbResultId = db.insert(TABLE_RESULT_IMAGE, null, resultImageCV)

        db.close()
        return dbResultId
    }

    fun getOriginalImageById(id: Int): DBOriginalImage? {
        val query = "SELECT * FROM $TABLE_ORIGINAL_IMAGE WHERE $COLUMN_PK_ORIGINAL_ID = '${id}'"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        if (db != null) {
            cursor = db.rawQuery(query, null)
        }

        if (cursor?.count != 0) {
            cursor?.moveToFirst()
            val id = cursor?.getInt(0)
            val byteArray = cursor?.getBlob(1)
            val created = cursor?.getString(2)

            return DBOriginalImage(id, byteArray, created)
        }

        db.close()
        cursor.close()
        return null
    }

    fun getAllOriginalImages(): ArrayList<DBOriginalImage> {
        val query = "SELECT * FROM $TABLE_ORIGINAL_IMAGE"
        val db = this.readableDatabase
        val resultList: ArrayList<DBOriginalImage> = arrayListOf()
        var cursor: Cursor? = null

        if (db != null) {
            cursor = db.rawQuery(query, null)
        }

        if (cursor?.count != 0) {
            while (cursor?.moveToNext() == true) {
                val id = cursor.getInt(0)
                val byteArray = cursor.getBlob(1)
                val created = cursor.getString(2)
                resultList.add(DBOriginalImage(id, byteArray, created))
            }
        }

        db.close()
        cursor?.close()
        return resultList
    }

    fun getAllResultImages(): ArrayList<DBResultImage> {
        val query = "SELECT * FROM $TABLE_RESULT_IMAGE"
        val db = this.readableDatabase
        val resultList: ArrayList<DBResultImage> = arrayListOf()
        var cursor: Cursor? = null

        if (db != null) {
            cursor = db.rawQuery(query, null)
        }

        if (cursor?.count != 0) {
            while (cursor?.moveToNext() == true) {
                val id = cursor.getInt(0)
                val storeLink = cursor.getString(1)
                val name = cursor.getString(2)
                val domain = cursor.getString(3)
                val identifier = cursor.getString(4)
                val trackingID = cursor.getString(5)
                val thumbnailLink = cursor.getString(6)
                val description = cursor.getString(7)
                val imageLink = cursor.getString(8)
                val imageBlob = cursor.getBlob(9)
                val currentDate = cursor.getString(10)
                val originalImageID = cursor.getInt(11)
                resultList.add(
                    DBResultImage(
                        id,
                        storeLink,
                        name,
                        domain,
                        identifier,
                        trackingID,
                        thumbnailLink,
                        description,
                        imageLink,
                        imageBlob,
                        currentDate,
                        originalImageID
                    )
                )
            }
        }

        cursor?.close()
        db.close()
        return resultList
    }

    fun getListOfResultsById(originalImageId: Int): ArrayList<DBResultImage> {
        val query = "SELECT * FROM $TABLE_RESULT_IMAGE WHERE $COLUMN_FK_ORIGINAL_IMG_ID = '${originalImageId}'"
        val db = this.readableDatabase
        val resultList: ArrayList<DBResultImage> = arrayListOf()
        var cursor: Cursor? = null

        if (db != null) {
            cursor = db.rawQuery(query, null)
        }

        if (cursor?.count != 0) {

            while (cursor?.moveToNext() == true) {
                val id = cursor.getInt(0)
                val storeLink = cursor.getString(1)
                val name = cursor.getString(2)
                val domain = cursor.getString(3)
                val identifier = cursor.getString(4)
                val trackingID = cursor.getString(5)
                val thumbnailLink = cursor.getString(6)
                val description = cursor.getString(7)
                val imageLink = cursor.getString(8)
                val imageBlob = cursor.getBlob(9)
                val currentDate = cursor.getString(10)
                val originalImageID = cursor.getInt(11)
                resultList.add(
                    DBResultImage(
                        id,
                        storeLink,
                        name,
                        domain,
                        identifier,
                        trackingID,
                        thumbnailLink,
                        description,
                        imageLink,
                        imageBlob,
                        currentDate,
                        originalImageID
                    )
                )
            }
        }

        cursor?.close()
        db.close()
        return resultList
    }

    fun getResultImageById(id: Int) : DBResultImage? {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_RESULT_IMAGE WHERE $COLUMN_PK_RESULT_ID = '${id}'"
        var cursor: Cursor? = null
        var dbResultImage: DBResultImage? = null

        if (db != null) {
            cursor = db.rawQuery(query, null)
        }

        if (cursor?.count != 0) {

            cursor?.moveToFirst()
            val id = cursor?.getInt(0)
            val storeLink = cursor?.getString(1)
            val name = cursor?.getString(2)
            val domain = cursor?.getString(3)
            val identifier = cursor?.getString(4)
            val trackingID = cursor?.getString(5)
            val thumbnailLink = cursor?.getString(6)
            val description = cursor?.getString(7)
            val imageLink = cursor?.getString(8)
            val imageBlob = cursor?.getBlob(9)
            val currentDate = cursor?.getString(10)
            val originalImageID = cursor?.getInt(11)

            originalImageID?.let {
                dbResultImage = DBResultImage(
                    id,
                    storeLink,
                    name,
                    domain,
                    identifier,
                    trackingID,
                    thumbnailLink,
                    description,
                    imageLink,
                    imageBlob,
                    currentDate,
                    originalImageID
                )
            }
        }

        cursor?.close()
        db.close()
        return dbResultImage
    }

    fun deleteResultImageById(id: Int){
        val db = this.writableDatabase
        val query = "DELETE FROM $TABLE_RESULT_IMAGE WHERE $COLUMN_PK_RESULT_ID = '${id}'"

        db.execSQL(query)
        db.close()
    }

    fun deleteResultImagesByOriginalImageId(id: Int){
        val db = this.writableDatabase
        val query = "DELETE FROM $TABLE_RESULT_IMAGE WHERE $COLUMN_FK_ORIGINAL_IMG_ID = '${id}'"

        db.execSQL(query)
        db.close()
    }

    fun deleteOriginalImageById(id: Int) {
        val db = this.writableDatabase
        val query = "DELETE FROM $TABLE_ORIGINAL_IMAGE WHERE $COLUMN_PK_ORIGINAL_ID = '${id}'"

        db.execSQL(query)
        db.close()
    }
}