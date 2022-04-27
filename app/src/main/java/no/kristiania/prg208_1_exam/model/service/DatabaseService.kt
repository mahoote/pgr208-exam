package no.kristiania.prg208_1_exam.model.service

import android.content.Context
import android.net.Uri
import android.util.Log
import no.kristiania.prg208_1_exam.model.db.DataBaseRepository
import no.kristiania.prg208_1_exam.models.DBOriginalImage
import no.kristiania.prg208_1_exam.models.DBResultImage
import kotlin.collections.ArrayList

class DatabaseService(context: Context) {

    private val dbRepo = DataBaseRepository(context)

    fun clear(){
        dbRepo.clear()
    }

    fun getResultImageByImageLink(imageLink: String): DBResultImage? {
        val result = dbRepo.getResultImageByImageLink(imageLink)

        Log.d("bitmap_debug", "getResultImageByImageLink: result: $result")

        close()
        return result
    }

    fun putResultImages(dbResultImages: ArrayList<DBResultImage>) : Boolean {
        // TODO: Check if bitmap exists.
        val result = dbRepo.putResultImages(dbResultImages)
        close()
        return result
    }

    fun deleteResultImageByImageLink(imageLink: String) {
        dbRepo.deleteResultImageByImageLink(imageLink)
        close()
    }

    fun getListOfResultsById(id: Int): ArrayList<DBResultImage> {
        val result = dbRepo.getListOfResultsById(id)
        close()
        return result
    }

    fun deleteOriginalAndResults(id: Int) {
        dbRepo.deleteOriginalAndResults(id)
        close()
    }

    fun getOriginalImageByUri(imageUri: String): DBOriginalImage? {
        val originals = dbRepo.getAllOriginalImages()
        close()
        var dbOriginalImage: DBOriginalImage? = null
        for (o in originals) {
            if(o.uri == Uri.parse(imageUri)) {
                dbOriginalImage = o
            }
        }
        if(dbOriginalImage != null) {
            return dbOriginalImage
        }
        // TODO: No image found.
        Log.d("db", "returning" + dbOriginalImage.toString())
        return dbOriginalImage
    }

    fun putOriginalImage(dbOriginalImage: DBOriginalImage) : Boolean {
        val result = dbRepo.putOriginalImage(dbOriginalImage)
        close()
        return result
    }

    fun close() {
        dbRepo.close()
    }

    fun getAllResultImages(): ArrayList<DBResultImage> {
        val result = dbRepo.getAllResultImages()
        close()
        return result
    }

    fun getAllOriginalImages(): ArrayList<DBOriginalImage> {
        val result = dbRepo.getAllOriginalImages()
        close()
        return result
    }

    fun getListOfResultsAsSearchItemById(id: Int): ArrayList<DBResultImage> {
        val result = dbRepo.getListOfResultsById(id)
        close()
        return result
    }
}