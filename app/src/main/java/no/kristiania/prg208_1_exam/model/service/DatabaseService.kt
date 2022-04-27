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
        dbRepo.close()
        return dbRepo.getResultImageByImageLink(imageLink)
    }

    fun putResultImages(dbResultImages: ArrayList<DBResultImage>) : Boolean {
        // TODO: Check if bitmap exists.
        dbRepo.close()
        return dbRepo.putResultImages(dbResultImages)
    }

    fun deleteResultImageByImageLink(imageLink: String) {
        dbRepo.close()
        dbRepo.deleteResultImageByImageLink(imageLink)
    }

    fun getListOfResultsById(id: Int): ArrayList<DBResultImage> {
        dbRepo.close()
        return dbRepo.getListOfResultsById(id)
    }

    fun deleteOriginalAndResults(id: Int) {
        dbRepo.close()
        dbRepo.deleteOriginalAndResults(id)
    }

    fun getOriginalImageByUri(imageUri: String): DBOriginalImage? {
        val originals = dbRepo.getAllOriginalImages()
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
        dbRepo.close()
        return dbRepo.putOriginalImage(dbOriginalImage)
    }

    fun close() {
        dbRepo.close()
    }

    fun getAllResultImages(): ArrayList<DBResultImage> {
        return dbRepo.getAllResultImages()
    }

    fun getAllOriginalImages(): ArrayList<DBOriginalImage> {
        return dbRepo.getAllOriginalImages()
    }

    fun getListOfResultsAsSearchItemById(id: Int): ArrayList<DBResultImage> {
        return dbRepo.getListOfResultsById(id)
    }
}