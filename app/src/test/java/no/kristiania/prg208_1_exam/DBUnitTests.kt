package no.kristiania.prg208_1_exam


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
       db.close()
    }

    @Test
    @Throws(Exception::class)
    fun shouldPutOriginalImages(){
        val img1 = DBOriginalImage(null, "testuri1", "now")
        val img2 = DBOriginalImage(null, "testuri2", "later")
        val img3 = DBOriginalImage(null, "testuri3", "before")

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

        val code1 = db.putResultImages(resImg1)
        val code2 = db.putResultImages(resImg2)
        val code3 = db.putResultImages(resImg3)

        assertEquals(true, code1)
        assertEquals(true, code2)
        assertEquals(true, code3)
    }

    @Test
    fun shouldGetAllResultImages(){
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

        db.putResultImages(resImg1)
        db.putResultImages(resImg2)
        db.putResultImages(resImg3)

        val dbResultImages = db.getAllResultImages()

        assertEquals(dbResultImages.size, 3)
    }

    @Test
    fun shouldGetAllOriginalImages(){
        val img1 = DBOriginalImage(null, "testuri1", "now")
        val img2 = DBOriginalImage(null, "testuri2", "later")
        val img3 = DBOriginalImage(null, "testuri3", "before")

        db.putOriginalImage(img1)
        db.putOriginalImage(img2)
        db.putOriginalImage(img3)

        val dbOriginalImages = db.getAllOriginalImages()

        assertEquals(dbOriginalImages.size, 3)
    }

    @Test
    fun shouldGetOriginalImageByUri(){
        val imageUri = "testuri1"
        val img1 = DBOriginalImage(null, imageUri, "now")
        db.putOriginalImage(img1)

        val returnedImage = db.getOriginalImageByUri(imageUri)

        assertEquals(imageUri, returnedImage?.uri)
    }

    @Test
    fun shouldGetListOfResultImagesById(){
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

        db.putResultImages(resImg1)
        db.putResultImages(resImg2)
        db.putResultImages(resImg3)

        val list = db.getListOfResultsById(1)

        list.forEach { img -> assertEquals(1, img.originalImgID) }
    }
}