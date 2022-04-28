package no.kristiania.prg208_1_exam.view.fragments

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import no.kristiania.prg208_1_exam.controller.utils.Utils
import no.kristiania.prg208_1_exam.controller.utils.Utils.toDp
import no.kristiania.prg208_1_exam.controller.utils.Utils.toUrl
import no.kristiania.prg208_1_exam.R
import no.kristiania.prg208_1_exam.controller.service.DatabaseService
import no.kristiania.prg208_1_exam.model.models.DBOriginalImage
import no.kristiania.prg208_1_exam.model.models.DBResultImage
import java.util.*

interface OnDataPass {
    fun onDataPass(data: Pair<Int, DBResultImage>)
}

class ChosenImageFragment : Fragment() {

    lateinit var dataPasser: OnDataPass

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnDataPass
    }

    private lateinit var imageView: ImageView
    private lateinit var chosenBitmapImage: Bitmap
    private lateinit var origBitmapImage: Bitmap
    private lateinit var chosenDbResultImage: DBResultImage
    private lateinit var dbService: DatabaseService
    private var origDBImageId: Long = 0
    private var chosenImagePosition: Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.fragment_chosen_image, container, false)

        dbService = DatabaseService(requireContext())
        imageView = v.findViewById(R.id.cif_chosen_img)

        val argsDbResultImage: DBResultImage? = arguments?.getParcelable("dbResultImage")
        val argsChosenBitmap: Bitmap? = arguments?.getParcelable("chosenBitmapImage")
        val argsOrigBitmap: Bitmap? = arguments?.getParcelable("originalBitmapImage")
        val argsOrigDBImageId: Long? = arguments?.getLong("origDBImageId")
        chosenImagePosition = arguments?.getInt("chosenImagePosition")!!

        val bookmarkBtn = v.findViewById<ImageButton>(R.id.cif_bookmark_btn)
        val webBtn = v.findViewById<ImageButton>(R.id.cif_web_btn)
        val nameView = v.findViewById<TextView>(R.id.cif_img_name_txt)
        val descView = v.findViewById<TextView>(R.id.cif_img_desc_view)

        argsDbResultImage?.let {
            chosenDbResultImage = it
        }
        argsChosenBitmap?.let {
            chosenBitmapImage = it
        }
        argsOrigBitmap?.let {
            origBitmapImage = it
        }
        argsOrigDBImageId?.let {
            origDBImageId = it
        }

        if(chosenDbResultImage.id == null && chosenDbResultImage.originalImgID != null) {
            bookmarkBtn.visibility = View.GONE
        }
        if(chosenDbResultImage.storeLink == null) {
            webBtn.visibility = View.GONE
        }

        imageView.setImageBitmap(chosenBitmapImage)

        nameView.text = chosenDbResultImage.name
        descView.text = chosenDbResultImage.description

        imageView.post {
            sizeCheck(imageView)
        }

        chosenDbResultImage.id?.let { setCorrectBookmarkIcon(bookmarkBtn, it) }

        // Close onclick
        v.findViewById<ImageButton>(R.id.cif_close_btn).setOnClickListener {
            closeFragment()
        }
        // Bookmark onclick
        bookmarkBtn.setOnClickListener {
            bookmarkBtnClicked(chosenDbResultImage, bookmarkBtn)
        }
        // Web onclick
        webBtn.setOnClickListener {
            webBtnClicked(chosenDbResultImage)
        }

        return v
    }

    private fun closeFragment() {
        parentFragmentManager.beginTransaction().remove(this).commit()
    }

    private fun setCorrectBookmarkIcon(bookmarkBtn: ImageButton, id: Int) {
        val dbResultImage = dbService.getResultImageById(id)
        dbResultImage?.let {
            bookmarkBtn.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_bookmark_solid
                )
            )
        }
    }

    private fun webBtnClicked(dbResultImage: DBResultImage) {
        // TODO: Refer to elvis
        dbResultImage.storeLink?.let { link -> toUrl(requireContext(), link) } ?: run {
            Toast.makeText(requireContext(), "Unable to get URL", Toast.LENGTH_SHORT).show()
        }
    }


    private fun bookmarkBtnClicked(dbResultImage: DBResultImage, bookmarkBtn: ImageButton) {
        val chosenByteArray = Utils.bitmapToByteArray(chosenBitmapImage)

        if (dbResultImage.id?.let { dbService.getResultImageById(it) } == null) {
            val originalImage = originalImageExists(dbService, origBitmapImage)

            val childDbResultImage = convertFormat(originalImage, dbResultImage, chosenByteArray)

            if (childDbResultImage != null) {
                val resultId = dbService.putResultImage(childDbResultImage)
                bookmarkBtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_bookmark_solid
                    )
                )
                chosenDbResultImage = dbService.getResultImageById(resultId.toInt())!!
            }
        } else {
            val chosenDbResult = dbService.getResultImageById(dbResultImage.id)
            val origId = chosenDbResult?.originalImgID

            dbService.deleteResultImageById(dbResultImage.id)
            bookmarkBtn.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_bookmark_stroke
                )
            )

            if (origId != null) {
                val results = dbService.getListOfResultsById(origId)
                if (results.isNullOrEmpty()) {
                    dbService.deleteOriginalAndResults(origId)
                }
            }

            closeFragment()
        }
    }

    private fun convertFormat(originalImage: DBOriginalImage?, dbResultImage: DBResultImage, byteArray: ByteArray): DBResultImage? {
        return originalImage?.id?.let { id ->
            Utils.convertResultImageToDBModel(id, dbResultImage, byteArray)
        }
    }

    private fun originalImageExists(dbService: DatabaseService, bitmapImage: Bitmap): DBOriginalImage? {
        val byteArray = Utils.bitmapToByteArray(bitmapImage)

        if (dbService.getOriginalImageById(origDBImageId.toInt()) == null) {
            dbService.putOriginalImage(
                DBOriginalImage(
                    origDBImageId.toInt(), byteArray,
                    Calendar.getInstance().time.toString()
                )
            )
        }

        return dbService.getOriginalImageById(origDBImageId.toInt())
    }

    private fun sizeCheck(imageView: ImageView) {
        val width = imageView.width
        val height = imageView.height
        val maxSize = 350f.toDp(requireContext())

        if(width > height) {
            imageView.layoutParams.width = maxSize
        } else {
            imageView.layoutParams.height = maxSize
        }
        imageView.requestLayout()
    }

    private fun passData(data: Pair<Int, DBResultImage>) {
        dataPasser.onDataPass(data)
    }

    // TODO: Reference to onDestroy.
    override fun onDestroy() {
        passData(Pair(chosenImagePosition, chosenDbResultImage))
        super.onDestroy()
    }
}