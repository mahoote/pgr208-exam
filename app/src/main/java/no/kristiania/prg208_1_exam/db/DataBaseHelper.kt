package no.kristiania.prg208_1_exam.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.util.Log
import no.kristiania.prg208_1_exam.models.DBOriginalImage
import no.kristiania.prg208_1_exam.models.DBResultImage
import no.kristiania.prg208_1_exam.models.ResultImage
import no.kristiania.prg208_1_exam.models.SearchItem

class DataBaseHelper(
    context: Context?,
) : SQLiteOpenHelper(context, "prg208.db", null, 1) {

    companion object {
        const val TABLE_ORIGINAL_IMAGE = "ORIGINAL_IMAGE"
        const val COLUMN_PK_ORIGINAL_ID = "ID"
        const val COLUMN_IMAGE_URI = "IMAGE_URL"
        const val COLUMN_CREATED = "CREATED"

        const val TABLE_RESULT_IMAGE = "RESULT_IMAGE"
        const val COLUMN_PK_RESULT_ID = "ID"
        const val COLUMN_FK_ORIGINAL_IMG_ID = "SEARCH_IMG_ID"
        const val COLUMN_STORE_LINK = "STORE_LINK"
        const val COLUMN_NAME = "NAME"
        const val COLUMN_DOMAIN = "DOMAIN"
        const val COLUMN_IDENTIFIER = "IDENTIFIER"
        const val COLUMN_TRACKING_ID = "TRACKING_ID"
        const val COLUMN_THUMBNAIL_LINK = "THUMBNAIL_LINK"
        const val COLUMN_DESCRIPTION = "DESCRIPTION"
        const val COLUMN_IMAGE_LINK = "IMAGE_LINK"
        const val COLUMN_CURRENT_DATE = "CURR_DATE"
    }

    // TODO: Check injection safety + close resources

    override fun onCreate(db: SQLiteDatabase?) {
        val createSearchImageTable =
            "CREATE TABLE $TABLE_ORIGINAL_IMAGE " +
                    "($COLUMN_PK_ORIGINAL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "$COLUMN_IMAGE_URI TEXT, " +
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
                    "$COLUMN_IMAGE_LINK TEXT, " +
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


    fun putOriginalImage(dbOriginalImage: DBOriginalImage): Boolean {
        val db: SQLiteDatabase = this.writableDatabase
        val originalImageCV = ContentValues()

        originalImageCV.put(COLUMN_IMAGE_URI, dbOriginalImage.uri.toString())
        originalImageCV.put(COLUMN_CREATED, dbOriginalImage.created)

        val origImgInsertStatus = db.insert(TABLE_ORIGINAL_IMAGE, null, originalImageCV)

        // TODO: Error handling
        if (origImgInsertStatus < 0) {
            closeDB(db)
            return false
        }
        closeDB(db)
        return true
    }

    fun putResultImages(dbResultImages: ArrayList<DBResultImage>): Boolean {
        val resultImageCV = ContentValues()
        val db = this.writableDatabase

        dbResultImages.forEach { img ->
            resultImageCV.put(COLUMN_STORE_LINK, img.storeLink)
            resultImageCV.put(COLUMN_NAME, img.name)
            resultImageCV.put(COLUMN_DOMAIN, img.domain)
            resultImageCV.put(COLUMN_IDENTIFIER, img.identifier)
            resultImageCV.put(COLUMN_TRACKING_ID, img.trackingID)
            resultImageCV.put(COLUMN_THUMBNAIL_LINK, img.thumbnailLink)
            resultImageCV.put(COLUMN_DESCRIPTION, img.description)
            resultImageCV.put(COLUMN_IMAGE_LINK, img.imageLink)
            resultImageCV.put(COLUMN_CURRENT_DATE, img.currentDate)
            resultImageCV.put(COLUMN_FK_ORIGINAL_IMG_ID, img.originalImgID)

            val resultInsertStatus = db.insert(TABLE_RESULT_IMAGE, null, resultImageCV)

            // TODO: Error handling
            if (resultInsertStatus < 0) {
                closeDB(db)
                return false
            }
        }
        closeDB(db)
        return true
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
            // there is data
            while (cursor?.moveToNext() == true) {
                val id = cursor.getInt(0)
                val uri = cursor.getString(1)
                val created = cursor.getString(2)
                resultList.add(DBOriginalImage(id, Uri.parse(uri), created))
            }
        } else {
            // TODO: Error handling: there is no data.
        }
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
            // there is data
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
                val currentDate = cursor.getString(9)
                val originalImageID = cursor.getInt(10)
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
                        currentDate,
                        originalImageID
                    )
                )
            }
        } else {
            // TODO: Error handling: there is no data.
        }
        return resultList
    }

    private fun closeDB(db: SQLiteDatabase){
        db.close()
    }

    fun clear() {
        this.writableDatabase?.execSQL("VACUUM")
    }

    fun getOriginalImageByUri(imageUri: String): DBOriginalImage? {
        val originals = getAllOriginalImages()
        var dbOriginalImage: DBOriginalImage? = null
        Log.d("db", "input uri :" + imageUri)

        for (o in originals) {
            Log.d("db", "correct uri :" + o.uri)
            if(o.uri == Uri.parse(imageUri)) {
                dbOriginalImage = o
            }
        }
        if(dbOriginalImage != null) {
            return dbOriginalImage
        }
        // TODO: No image found.
        return dbOriginalImage
    }

    fun getListOfResultsById(originalImageId: Int): ArrayList<DBResultImage> {
        val query = "SELECT * FROM $TABLE_RESULT_IMAGE WHERE $COLUMN_FK_ORIGINAL_IMG_ID = $originalImageId"
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
                val currentDate = cursor.getString(9)
                val originalImageID = cursor.getInt(10)
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
                        currentDate,
                        originalImageID
                    )
                )
            }
        } else {
            // TODO: Error handling: there is no data.
        }
        return resultList
    }

    fun getListOfResultsAsSearchItemById(originalImageId: Int): ArrayList<SearchItem> {
        val query = "SELECT * FROM $TABLE_RESULT_IMAGE WHERE $COLUMN_FK_ORIGINAL_IMG_ID = $originalImageId"
        val db = this.readableDatabase

        val resultList: ArrayList<SearchItem> = arrayListOf()

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
                val currentDate = cursor.getString(9)
                val originalImageID = cursor.getInt(10)
                resultList.add(
                    SearchItem(
                        id,
                        Uri.parse(imageLink),
                        false
                    )
                )
            }
        } else {
            // TODO: Error handling: there is no data.
        }
        return resultList
    }


}