package no.kristiania.prg208_1_exam.model.service

import android.content.Context
import android.util.Log
import no.kristiania.prg208_1_exam.model.db.DataBaseRepository
import no.kristiania.prg208_1_exam.models.DBOriginalImage
import no.kristiania.prg208_1_exam.models.DBResultImage
import java.util.*
import kotlin.collections.ArrayList

class DatabaseService(context: Context) {

    private val dbRepo = DataBaseRepository(context)

    fun clear(){
        dbRepo.clear()
    }

    fun getResultImageById(id: Int): DBResultImage? {
        val result = dbRepo.getResultImageById(id)
        close()
        return result
    }

    fun putResultImage(dbResultImage: DBResultImage) : Long {
        // TODO: Check if bitmap exists.
        val resultId = dbRepo.putResultImage(dbResultImage)
        Log.d("db_debug", "putResultImage: Result put? $resultId")
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
        dbRepo.deleteOriginalAndResults(id)
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

    private fun origArrayEquals(
        inputArray: ArrayList<DBOriginalImage>,
        byteArray: ByteArray
    ): DBOriginalImage? {
        inputArray.forEach { origImage ->
            if (Arrays.equals(origImage.byteArray, byteArray)) {
                return origImage
            }
        }
        return null
    }

    private fun resultArrayEquals(
        inputArray: ArrayList<DBResultImage>,
        byteArray: ByteArray
    ): DBResultImage? {
        inputArray.forEach { resultImage ->
            if (Arrays.equals(resultImage.imageBlob, byteArray)) {
                return resultImage
            }
        }
        return null
    }
}