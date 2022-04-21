package no.kristiania.prg208_1_exam.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import no.kristiania.prg208_1_exam.models.CachedImages
import no.kristiania.prg208_1_exam.models.DBOriginalImage
import no.kristiania.prg208_1_exam.models.DBResultImage

class DataBaseHelper(
    context: Context?,
) : SQLiteOpenHelper(context, "prg208.db", null, 1) {

    companion object {
        const val TABLE_ORIGINAL_IMAGE = "SEARCH_IMAGE"
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

    // TODO: Check injection safety

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
        TODO("Not yet implemented")
    }

    fun deleteByUri(uri: Uri): Boolean {
        val db = this.writableDatabase
        val originalImageQueryString =
            "DELETE FROM $TABLE_ORIGINAL_IMAGE WHERE $COLUMN_IMAGE_URI = $uri"

        val originalImage: DBOriginalImage? = getOriginalImageByUri(uri)

        if (originalImage != null) {
            val resultImageQueryString =
                "DELETE FROM $TABLE_RESULT_IMAGE WHERE $COLUMN_FK_ORIGINAL_IMG_ID = ${originalImage?.id}"
            db.execSQL(resultImageQueryString)
        } else {
            return false
        }
        return true
    }

    fun addSearch(cachedImages: CachedImages): Boolean {
        val db: SQLiteDatabase = this.writableDatabase

        val searchImageCV = ContentValues()

        searchImageCV.put(COLUMN_IMAGE_URI, cachedImages.imageUri.toString())
        searchImageCV.put(COLUMN_CREATED, cachedImages.created.toString())

        addResultImages(cachedImages)

        val searchInsertStatus = db.insert(TABLE_ORIGINAL_IMAGE, null, searchImageCV)

        // TODO: Error handling
        if (searchInsertStatus < 0) {
            return false
        }

        db.close()
        if(!addResultImages(cachedImages)){
            return false
        }
        return true
    }

    private fun addResultImages(cachedImages: CachedImages): Boolean {
        val resultImageCV = ContentValues()
        val db = this.writableDatabase

        resultImageCV.put(
            COLUMN_STORE_LINK,
            cachedImages.images.map { image -> image?.store_link }.toString()
        )
        resultImageCV.put(COLUMN_NAME, cachedImages.images.map { image -> image?.name }.toString())
        resultImageCV.put(
            COLUMN_DOMAIN,
            cachedImages.images.map { image -> image?.domain }.toString()
        )
        resultImageCV.put(
            COLUMN_IDENTIFIER,
            cachedImages.images.map { image -> image?.identifier }.toString()
        )
        resultImageCV.put(
            COLUMN_TRACKING_ID,
            cachedImages.images.map { image -> image?.tracking_id }.toString()
        )
        resultImageCV.put(
            COLUMN_THUMBNAIL_LINK,
            cachedImages.images.map { image -> image?.thumbnail_link }.toString()
        )
        resultImageCV.put(
            COLUMN_DESCRIPTION,
            cachedImages.images.map { image -> image?.description }.toString()
        )
        resultImageCV.put(
            COLUMN_IMAGE_LINK,
            cachedImages.images.map { image -> image?.image_link }.toString()
        )
        resultImageCV.put(
            COLUMN_CURRENT_DATE,
            cachedImages.images.map { image -> image?.current_date }.toString()
        )

        val resultInsertStatus = db.insert(TABLE_RESULT_IMAGE, null, resultImageCV)
        db.close()
        if(resultInsertStatus < 0) {
            return false
        }
        return true
    }

    private fun getAllOriginalImages(): ArrayList<DBOriginalImage> {
        val list = arrayListOf<DBOriginalImage>()

        val queryString = "SELECT * FROM $TABLE_ORIGINAL_IMAGE"
        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val uri = cursor.getString(1)
                val created = cursor.getString(2)

                val originalImage = DBOriginalImage(id, uri, created)
                list.add(originalImage)
            } while (cursor.moveToFirst())
        } else {
            // No results, empty list will be returned.
        }
        db.close()
        cursor.close()
        return list
    }

    private fun getAllResultImages(): ArrayList<DBResultImage> {
        val list = arrayListOf<DBResultImage>()

        val queryString = "SELECT * FROM $TABLE_RESULT_IMAGE"
        val db = this.readableDatabase
        val cursor = db.rawQuery(queryString, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val storeLink = cursor.getString(1)
                val name = cursor.getString(2)
                val domain = cursor.getString(3)
                val identifier = cursor.getString(4)
                val trackingID = cursor.getString(5)
                val thumbnailLink = cursor.getString(6)
                val desc = cursor.getString(7)
                val imageLink = cursor.getString(8)
                val currentDate = cursor.getString(9)
                val originalImgId = cursor.getString(10)

                val resultImage = DBResultImage(
                    id,
                    storeLink,
                    name,
                    domain,
                    identifier,
                    trackingID,
                    thumbnailLink,
                    desc,
                    imageLink,
                    currentDate,
                    originalImgId
                )
                list.add(resultImage)
            } while (cursor.moveToFirst())
        } else {
            // No results, empty list will be returned.
        }
        db.close()
        cursor.close()
        return list
    }

    private fun getOriginalImageByUri(uri: Uri): DBOriginalImage? {
        val db = this.readableDatabase
        val queryString = "SELECT * FROM $TABLE_ORIGINAL_IMAGE WHERE $COLUMN_IMAGE_URI = $uri"
        val cursor = db.rawQuery(queryString, null)

        var originalImage: DBOriginalImage? = null

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val uri = cursor.getString(1)
                val created = cursor.getString(2)
                originalImage = DBOriginalImage(id, uri, created)

            } while (cursor.moveToFirst())
        } else {
            // Nothing found
        }
        db.close()
        cursor.close()
        return originalImage
    }


    fun sortSearches(): ArrayList<CachedImages> {
        val originalImages = getAllOriginalImages()
        val resultImages = getAllResultImages()
        val list = arrayListOf<CachedImages>()

        return arrayListOf()
    }
}