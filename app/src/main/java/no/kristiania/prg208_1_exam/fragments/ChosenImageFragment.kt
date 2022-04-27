package no.kristiania.prg208_1_exam.fragments

import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import no.kristiania.prg208_1_exam.Globals
import no.kristiania.prg208_1_exam.Globals.picassoLoad
import no.kristiania.prg208_1_exam.Globals.toDp
import no.kristiania.prg208_1_exam.Globals.toUrl
import no.kristiania.prg208_1_exam.R
import no.kristiania.prg208_1_exam.model.db.DataBaseRepository
import no.kristiania.prg208_1_exam.model.service.DatabaseService
import no.kristiania.prg208_1_exam.models.DBOriginalImage
import no.kristiania.prg208_1_exam.models.DBResultImage
import no.kristiania.prg208_1_exam.models.ResultImage
import java.util.*
import kotlin.collections.ArrayList

class ChosenImageFragment : Fragment() {

    var TAG = "bitmap_debug"

    private lateinit var imageView: ImageView
    private lateinit var resultImage: ResultImage
    private lateinit var dbService: DatabaseService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v =  inflater.inflate(R.layout.fragment_chosen_image, container, false)

        dbService = DatabaseService(requireContext())
        resultImage = arguments?.getSerializable("resultImage") as ResultImage
        imageView = v.findViewById(R.id.cif_chosen_img)

        val origImgUri: Uri? = Uri.parse(arguments?.getString("uriString"))
        val bookmarkBtn = v.findViewById<ImageButton>(R.id.cif_bookmark_btn)
        val nameView = v.findViewById<TextView>(R.id.cif_img_name_txt)
        val descView = v.findViewById<TextView>(R.id.cif_img_desc_view)

        picassoLoad(resultImage.image_link.toString(), imageView)
        nameView.text = resultImage.name
        descView.text = resultImage.description

        imageView.post {
            sizeCheck(imageView)

            // TODO: Save resultBitmap to db instead of link.
            val resultBitmap = Globals.drawableToBitmap(imageView.drawable as BitmapDrawable)
            Log.d(TAG, "onCreateView: resultBitmap: $resultBitmap")
        }

        setCorrectBookmarkIcon(bookmarkBtn)

        // Close onclick
        v.findViewById<ImageButton>(R.id.cif_close_btn).setOnClickListener {
            Log.i("debug", "Close")
            parentFragmentManager.beginTransaction().remove(this).commit()
        }
        // Bookmark onclick
        bookmarkBtn.setOnClickListener {
            bookmarkBtnClicked(origImgUri, resultImage, bookmarkBtn)
        }
        // Web onclick
        v.findViewById<ImageButton>(R.id.cif_web_btn).setOnClickListener {
            webBtnClicked(resultImage)
        }

        return v
    }

    private fun setCorrectBookmarkIcon(bookmarkBtn: ImageButton) {
        val dbResultImage = resultImage.image_link?.let { dbService.getResultImageByImageLink(it) }
        dbResultImage?.let {
            bookmarkBtn.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_bookmark_solid
                )
            )
        }
    }

    private fun webBtnClicked(resultImage: ResultImage) {
        // TODO: Refer to elvis
        resultImage.store_link?.let { it1 -> toUrl(requireContext(), it1) } ?: run {
            Toast.makeText(requireContext(), "Unable to get URL", Toast.LENGTH_SHORT).show()
        }
    }

    private fun bookmarkBtnClicked(origImgUri: Uri?, resultImage: ResultImage, bookmarkBtn: ImageButton) {
        val dbResultImage = resultImage.image_link?.let { dbService.getResultImageByImageLink(it) }

        if (dbResultImage == null) {
            val originalImage = originalImageExists(dbService, origImgUri)

            val resultImages = arrayListOf<ResultImage?>()
            resultImages.add(resultImage)

            val dbResultImages = convertFormat(originalImage, resultImages)

            if (!dbResultImages.isNullOrEmpty()) {
                dbService.putResultImages(dbResultImages)
                bookmarkBtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_bookmark_solid
                    )
                )
            }
        } else {
            val selectedImage = dbService.getResultImageByImageLink(resultImage.image_link)
            val origId = selectedImage?.originalImgID

            dbService.deleteResultImageByImageLink(resultImage.image_link.toString())
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
        }
    }

    private fun convertFormat(originalImage: DBOriginalImage?, resultImages: ArrayList<ResultImage?>): ArrayList<DBResultImage>? {
        return originalImage?.id?.let { ogi ->
            Globals.convertResultImagesToDBModel(resultImages, ogi)
        }
    }

    private fun originalImageExists(dbService: DatabaseService, origImgUri: Uri?): DBOriginalImage? {
        if (dbService.getOriginalImageByUri(origImgUri.toString()) == null) {
            dbService.putOriginalImage(
                DBOriginalImage(
                    null, origImgUri,
                    Calendar.getInstance().time.toString()
                )
            )
        }

        return dbService.getOriginalImageByUri(origImgUri.toString())
    }

    private fun sizeCheck(imageView: ImageView) {
        val width = imageView.width
        val height = imageView.height
        val maxSize = 350f.toDp(requireContext())

        if(width > height) {
            imageView.layoutParams.width = maxSize
            Log.d("p_debug", "sizeCheck: Width is bigger: ${imageView.layoutParams.width}")
        } else {
            imageView.layoutParams.height = maxSize
            Log.d("p_debug", "sizeCheck: Height is bigger or equal: ${imageView.layoutParams.height}")
        }
        imageView.requestLayout()
    }
}