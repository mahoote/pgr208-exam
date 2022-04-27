package no.kristiania.prg208_1_exam


import android.net.Uri
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import no.kristiania.prg208_1_exam.db.DataBaseHelper
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
    lateinit var db: DataBaseHelper


    @Before
    fun setup(){
        db = DataBaseHelper(ApplicationProvider.getApplicationContext())
        db.clear()
    }

    @After
    fun finish(){
        db.clear()
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun shouldPutOriginalImages(){
        val img1 = DBOriginalImage(null, Uri.parse("testuri1"), "now")
        val img2 = DBOriginalImage(null, Uri.parse("testuri2"), "later")
        val img3 = DBOriginalImage(null, Uri.parse("testuri3"), "before")

        val code1 = db.putOriginalImage(img1)
        val code2 = db.putOriginalImage(img2)
        val code3 = db.putOriginalImage(img3)

        assertEquals(true, code1)
        assertEquals(true, code2)
        assertEquals(true, code3)

        db.close()
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

        val code = db.putResultImages(resultImages)

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

        db.putResultImages(resultImages)

        val dbResultImages = db.getAllResultImages()

        assertEquals(dbResultImages.size, 3)
    }

    @Test
    fun shouldGetAllOriginalImages(){
        val img1 = DBOriginalImage(null, Uri.parse("testuri1"), "now")
        val img2 = DBOriginalImage(null, Uri.parse("testuri2"), "later")
        val img3 = DBOriginalImage(null, Uri.parse("testuri3"), "before")

        db.putOriginalImage(img1)
        db.putOriginalImage(img2)
        db.putOriginalImage(img3)

        val dbOriginalImages = db.getAllOriginalImages()

        assertEquals(dbOriginalImages.size, 3)
    }

    @Test
    fun shouldGetOriginalImageByUri(){
        val imageUri = "testuri1"
        val img1 = DBOriginalImage(null, Uri.parse(imageUri), "now")
        db.putOriginalImage(img1)

        val returnedImage = db.getOriginalImageByUri(imageUri)

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

        db.putResultImages(resultImages)

        val list = db.getListOfResultsById(1)

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

        db.putResultImages(resultImages)

        val list = db.getListOfResultsAsSearchItemById(1)

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

        db.putResultImages(resultImages)
        val dbResultImage = db.getResultImageByImageLink(Uri.parse("imageLink1"))
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

        db.putResultImages(resultImages)
        db.deleteResultImageByUri("imageLink1")
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

        db.putResultImages(resultImages)

        val imageUri = "testuri1"
        val img1 = DBOriginalImage(1, Uri.parse(imageUri), "now")
        db.putOriginalImage(img1)

        db.deleteOriginalAndResults(1)

        assertEquals(0, db.getAllResultImages().size)
        assertEquals(0, db.getAllOriginalImages().size)
    }
}