package no.kristiania.prg208_1_exam.model.service

import android.content.Context
import no.kristiania.prg208_1_exam.model.db.DataBaseRepository
import no.kristiania.prg208_1_exam.models.DBOriginalImage
import no.kristiania.prg208_1_exam.models.DBResultImage
import kotlin.collections.ArrayList

class DatabaseService(context: Context) {

    private val dbRepo = DataBaseRepository(context)

    fun getResultImageById(id: Int): DBResultImage? {
        val result = dbRepo.getResultImageById(id)

        close()
        return result
    }

    fun putResultImage(dbResultImage: DBResultImage) : Long {
        val resultId = dbRepo.putResultImage(dbResultImage)

        close()
        return resultId
    }

    fun deleteResultImageById(id: Int) {
        dbRepo.deleteResultImageById(id)

        close()
    }

    fun getListOfResultsById(id: Int): ArrayList<DBResultImage> {
        val result = dbRepo.getListOfResultsById(id)

        close()
        return result
    }

    fun deleteOriginalAndResults(id: Int) {
        dbRepo.deleteOriginalImageById(id)
        dbRepo.deleteResultImagesByOriginalImageId(id)

        close()
    }

    fun getOriginalImageById(id: Int): DBOriginalImage? {
        return dbRepo.getOriginalImageById(id)
    }

    fun putOriginalImage(dbOriginalImage: DBOriginalImage) : Long {
        val result = dbRepo.putOriginalImage(dbOriginalImage)

        close()
        return result
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

    fun close() {
        dbRepo.close()
    }
}