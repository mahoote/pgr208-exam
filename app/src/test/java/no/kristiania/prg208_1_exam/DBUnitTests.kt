/*
package no.kristiania.prg208_1_exam


import android.net.Uri
import androidx.test.core.app.ApplicationProvider
import no.kristiania.prg208_1_exam.model.service.DatabaseService
import no.kristiania.prg208_1_exam.models.DBOriginalImage
import no.kristiania.prg208_1_exam.models.DBResultImage
import org.junit.After
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.lang.Exception

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
class DBUnitTests {
    lateinit var dbService: DatabaseService


    @Before
    fun setup(){
        dbService = DatabaseService(ApplicationProvider.getApplicationContext())
        dbService.clear()
    }

    @After
    fun finish(){
        dbService.clear()
        dbService.close()
    }

    @Test
    @Throws(Exception::class)
    fun shouldPutOriginalImages(){
        val img1 = DBOriginalImage(null, Uri.parse("testuri1"), "now")
        val img2 = DBOriginalImage(null, Uri.parse("testuri2"), "later")
        val img3 = DBOriginalImage(null, Uri.parse("testuri3"), "before")

        val code1 = dbService.putOriginalImage(img1)
        val code2 = dbService.putOriginalImage(img2)
        val code3 = dbService.putOriginalImage(img3)

        assertEquals(true, code1)
        assertEquals(true, code2)
        assertEquals(true, code3)

        dbService.close()
    }

    @Test
    fun shouldPutResultImages(){
        val resultImages = arrayListOf<DBResultImage>()

        val resImg1 = DBResultImage(
            null,
            "storeLink1",
            "name1",
            "domain1",
            "identifier1",
            "trackingID1",
            "thumb1",
            "desc1",
            "imageLink1",
            "curDate1",
            1
        )
        val resImg2 = DBResultImage(
            null,
            "storeLink2",
            "name2",
            "domain2",
            "identifier2",
            "trackingID2",
            "thumb2",
            "desc2",
            "imageLink2",
            "curDate2",
            2
        )
        val resImg3 = DBResultImage(
            null,
            "storeLink3",
            "name3",
            "domain3",
            "identifier3",
            "trackingID3",
            "thumb3",
            "desc3",
            "imageLink3",
            "curDate3",
            3
        )

        resultImages.add(resImg1)
        resultImages.add(resImg2)
        resultImages.add(resImg3)

        val code = dbService.putResultImage(resultImages)

        assertEquals(true, code)
    }

    @Test
    fun shouldGetAllResultImages(){
        val resultImages = arrayListOf<DBResultImage>()
        val resImg1 = DBResultImage(
            null,
            "storeLink1",
            "name1",
            "domain1",
            "identifier1",
            "trackingID1",
            "thumb1",
            "desc1",
            "imageLink1",
            "curDate1",
            1
        )
        val resImg2 = DBResultImage(
            null,
            "storeLink2",
            "name2",
            "domain2",
            "identifier2",
            "trackingID2",
            "thumb2",
            "desc2",
            "imageLink2",
            "curDate2",
            2
        )
        val resImg3 = DBResultImage(
            null,
            "storeLink3",
            "name3",
            "domain3",
            "identifier3",
            "trackingID3",
            "thumb3",
            "desc3",
            "imageLink3",
            "curDate3",
            3
        )

        resultImages.add(resImg1)
        resultImages.add(resImg2)
        resultImages.add(resImg3)

        dbService.putResultImage(resultImages)

        val dbResultImages = dbService.getAllResultImages()

        assertEquals(dbResultImages.size, 3)
    }

    @Test
    fun shouldGetAllOriginalImages(){
        val img1 = DBOriginalImage(null, Uri.parse("testuri1"), "now")
        val img2 = DBOriginalImage(null, Uri.parse("testuri2"), "later")
        val img3 = DBOriginalImage(null, Uri.parse("testuri3"), "before")

        dbService.putOriginalImage(img1)
        dbService.putOriginalImage(img2)
        dbService.putOriginalImage(img3)

        val dbOriginalImages = dbService.getAllOriginalImages()

        assertEquals(dbOriginalImages.size, 3)
    }

    @Test
    fun shouldGetOriginalImageByUri(){
        val imageUri = "testuri1"
        val img1 = DBOriginalImage(null, Uri.parse(imageUri), "now")
        dbService.putOriginalImage(img1)

        val returnedImage = dbService.getOriginalImageById(imageUri)

        assertEquals(imageUri, returnedImage?.uri.toString())
    }

    @Test
    fun shouldGetListOfResultImagesById(){
        val resultImages = arrayListOf<DBResultImage>()

        val resImg1 = DBResultImage(
            null,
            "storeLink1",
            "name1",
            "domain1",
            "identifier1",
            "trackingID1",
            "thumb1",
            "desc1",
            "imageLink1",
            "curDate1",
            1
        )
        val resImg2 = DBResultImage(
            null,
            "storeLink2",
            "name2",
            "domain2",
            "identifier2",
            "trackingID2",
            "thumb2",
            "desc2",
            "imageLink2",
            "curDate2",
            1
        )
        val resImg3 = DBResultImage(
            null,
            "storeLink3",
            "name3",
            "domain3",
            "identifier3",
            "trackingID3",
            "thumb3",
            "desc3",
            "imageLink3",
            "curDate3",
            1
        )
        resultImages.add(resImg1)
        resultImages.add(resImg2)
        resultImages.add(resImg3)

        dbService.putResultImage(resultImages)

        val list = dbService.getListOfResultsById(1)

        list.forEach { img -> assertEquals(1, img.originalImgID) }
    }

    @Test
    fun shouldGetListOfResultImagesAsSearchItemsById(){
        val resultImages = arrayListOf<DBResultImage>()

        val resImg1 = DBResultImage(
            null,
            "storeLink1",
            "name1",
            "domain1",
            "identifier1",
            "trackingID1",
            "thumb1",
            "desc1",
            "imageLink1",
            "curDate1",
            1
        )
        val resImg2 = DBResultImage(
            null,
            "storeLink2",
            "name2",
            "domain2",
            "identifier2",
            "trackingID2",
            "thumb2",
            "desc2",
            "imageLink2",
            "curDate2",
            1
        )
        val resImg3 = DBResultImage(
            null,
            "storeLink3",
            "name3",
            "domain3",
            "identifier3",
            "trackingID3",
            "thumb3",
            "desc3",
            "imageLink3",
            "curDate3",
            1
        )
        resultImages.add(resImg1)
        resultImages.add(resImg2)
        resultImages.add(resImg3)

        dbService.putResultImage(resultImages)

        val list = dbService.getListOfResultsAsSearchItemById(1)

        assertEquals(3, list.size)
    }

    @Test
    fun shouldGetResultImageByImageLink() {
        val resultImages = arrayListOf<DBResultImage>()

        val resImg1 = DBResultImage(
            null,
            "storeLink1",
            "name1",
            "domain1",
            "identifier1",
            "trackingID1",
            "thumb1",
            "desc1",
            "imageLink1",
            "curDate1",
            1
        )

        resultImages.add(resImg1)

        dbService.putResultImage(resultImages)
        val dbResultImage = dbService.getResultImageById("imageLink1")
        assertEquals("imageLink1", dbResultImage?.imageLink)
    }

    @Test
    fun shouldDeleteResultImage(){
        val resultImages = arrayListOf<DBResultImage>()

        val resImg1 = DBResultImage(
            null,
            "storeLink1",
            "name1",
            "domain1",
            "identifier1",
            "trackingID1",
            "thumb1",
            "desc1",
            "imageLink1",
            "curDate1",
            1
        )

        resultImages.add(resImg1)

        dbService.putResultImage(resultImages)
        dbService.deleteResultImageById("imageLink1")
    }

    @Test
    fun shouldDeleteOriginalImageAndResultImages(){
        val resultImages = arrayListOf<DBResultImage>()

        val resImg1 = DBResultImage(
            null,
            "storeLink1",
            "name1",
            "domain1",
            "identifier1",
            "trackingID1",
            "thumb1",
            "desc1",
            "imageLink1",
            "curDate1",
            1
        )

        resultImages.add(resImg1)

        dbService.putResultImage(resultImages)

        val imageUri = "testuri1"
        val img1 = DBOriginalImage(1, Uri.parse(imageUri), "now")
        dbService.putOriginalImage(img1)

        dbService.deleteOriginalAndResults(1)

        assertEquals(0, dbService.getAllResultImages().size)
        assertEquals(0, dbService.getAllOriginalImages().size)
    }
}*/
