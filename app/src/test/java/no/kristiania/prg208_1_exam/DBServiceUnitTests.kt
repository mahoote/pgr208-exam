package no.kristiania.prg208_1_exam


import androidx.test.core.app.ApplicationProvider
import no.kristiania.prg208_1_exam.controller.service.DatabaseService
import no.kristiania.prg208_1_exam.model.models.DBOriginalImage
import no.kristiania.prg208_1_exam.model.models.DBResultImage
import org.junit.After
import org.junit.Test
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class DBServiceUnitTests {
    lateinit var dbService: DatabaseService

    @Before
    fun setup(){
        dbService = DatabaseService(ApplicationProvider.getApplicationContext())
    }

    @After
    fun finish(){
        dbService.close()
    }

    @Test
    fun shouldPutResultImage(){
        val testDBResImg = DBResultImage(
            null, "storeLink", "name",
            "domain", "identifier", "trackingid",
            "thumbnail", "description", "imageLink",
            byteArrayOf(), Calendar.getInstance().time.toString(), null
        )

        assert(dbService.putResultImage(testDBResImg) == 1L)

        dbService.close()
    }

    @Test
    fun shouldPutAndGetResultImage(){
        val testDBResImg = DBResultImage(
            null, "storeLink", "name",
            "domain", "identifier", "trackingid",
            "thumbnail", "description", "imageLink",
            byteArrayOf(), Calendar.getInstance().time.toString(), null
        )

        dbService.putResultImage(testDBResImg)

        val retrieved = dbService.getResultImageById(1)
        assert(retrieved?.storeLink.equals("storeLink"))
    }

    @Test
    fun shouldGetAllResultImages(){
        val testDBResImg1 = DBResultImage(
            null, "storeLink", "name",
            "domain", "identifier", "trackingid",
            "thumbnail", "description", "imageLink",
            byteArrayOf(), Calendar.getInstance().time.toString(), null
        )

        val testDBResImg2 = DBResultImage(
            null, "storeLink", "name",
            "domain", "identifier", "trackingid",
            "thumbnail", "description", "imageLink",
            byteArrayOf(), Calendar.getInstance().time.toString(), null
        )

        dbService.putResultImage(testDBResImg1)
        dbService.putResultImage(testDBResImg2)

        assert(dbService.getAllResultImages().size == 2)
    }

    @Test
    fun shouldPutAndDeleteResultImage(){
        val testDBResImg = DBResultImage(
            null, "storeLink", "name",
            "domain", "identifier", "trackingid",
            "thumbnail", "description", "imageLink",
            byteArrayOf(), Calendar.getInstance().time.toString(), null
        )

        dbService.putResultImage(testDBResImg)
        assert(dbService.getAllResultImages().size == 1)
        dbService.deleteResultImageById(1)
        assert(dbService.getAllOriginalImages().size == 0)
    }

    @Test
    fun shouldPutOriginalImage(){
        val testDBOrigImg = DBOriginalImage(null, byteArrayOf(), "created")

        assert(dbService.putOriginalImage(testDBOrigImg) == 1L)

        dbService.close()
    }

    @Test
    fun shouldPutAndGetOriginalImage(){
        val testDBOrigImg = DBOriginalImage(null, byteArrayOf(), "created")

        dbService.putOriginalImage(testDBOrigImg)

        assert(dbService.getOriginalImageById(1)?.created.equals("created"))
    }

    @Test
    fun shouldGetAllOriginalImages(){
        val testDBOrigImg1 = DBOriginalImage(null, byteArrayOf(), "created")
        val testDBOrigImg2 = DBOriginalImage(null, byteArrayOf(), "created")

        dbService.putOriginalImage(testDBOrigImg1)
        dbService.putOriginalImage(testDBOrigImg2)

        assert(dbService.getAllOriginalImages().size == 2)
    }

    @Test
    fun shouldDeleteOriginalAndResultsImages(){
        val testDBOrigImg = DBOriginalImage(null, byteArrayOf(), "created")

        dbService.putOriginalImage(testDBOrigImg)

        val testDBResImg1 = DBResultImage(
            null, "storeLink", "name",
            "domain", "identifier", "trackingid",
            "thumbnail", "description", "imageLink",
            byteArrayOf(), Calendar.getInstance().time.toString(), 1
        )

        val testDBResImg2 = DBResultImage(
            null, "storeLink", "name",
            "domain", "identifier", "trackingid",
            "thumbnail", "description", "imageLink",
            byteArrayOf(), Calendar.getInstance().time.toString(), 1
        )

        dbService.putResultImage(testDBResImg1)
        dbService.putResultImage(testDBResImg2)

        assert(dbService.getAllOriginalImages().size == 1)
        assert(dbService.getAllResultImages().size == 2)

        dbService.deleteOriginalAndResults(1)

        assert(dbService.getAllOriginalImages().size == 0)
        assert(dbService.getAllResultImages().size == 0)
    }

    @Test
    fun shouldGetListOfResultsById(){
        val testDBOrigImg = DBOriginalImage(null, byteArrayOf(), "created")

        dbService.putOriginalImage(testDBOrigImg)

        val testDBResImg1 = DBResultImage(
            null, "store1", "name",
            "domain", "identifier", "trackingid",
            "thumbnail", "description", "imageLink",
            byteArrayOf(), Calendar.getInstance().time.toString(), 1
        )

        val testDBResImg2 = DBResultImage(
            null, "store2", "name",
            "domain", "identifier", "trackingid",
            "thumbnail", "description", "imageLink",
            byteArrayOf(), Calendar.getInstance().time.toString(), 1
        )

        dbService.putResultImage(testDBResImg1)
        dbService.putResultImage(testDBResImg2)

        val list = dbService.getListOfResultsById(1)

        assert(list.size == 2)
        assert(list[0].storeLink.equals("store1"))
        assert(list[1].storeLink.equals("store2"))
    }
}
