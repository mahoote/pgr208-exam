package no.kristiania.prg208_1_exam.fragments

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
import no.kristiania.prg208_1_exam.Globals.toDp
import no.kristiania.prg208_1_exam.Globals.toUrl
import no.kristiania.prg208_1_exam.R
import no.kristiania.prg208_1_exam.db.DataBaseHelper
import no.kristiania.prg208_1_exam.models.DBOriginalImage
import no.kristiania.prg208_1_exam.models.DBResultImage
import no.kristiania.prg208_1_exam.models.ResultImage
import java.util.*
import kotlin.collections.ArrayList

class ChosenImageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_chosen_image, container, false)

        val origImgUri: Uri? = Uri.parse(arguments?.getString("uriString"))

        v.findViewById<ImageButton>(R.id.cif_close_btn).setOnClickListener {
            Log.i("debug", "Close")
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        val resultImage = arguments?.getSerializable("resultImage") as ResultImage
        val dbHelper = DataBaseHelper(context)

        val imageView = v.findViewById<ImageView>(R.id.cif_chosen_img)
        val nameView = v.findViewById<TextView>(R.id.cif_img_name_txt)
        val descView = v.findViewById<TextView>(R.id.cif_img_desc_view)

        Picasso.get().load(resultImage.image_link).placeholder(R.drawable.result_image_placeholder).into(imageView)

        imageView.post {
            sizeCheck(imageView)
        }

        nameView.text = resultImage.name
        descView.text = resultImage.description

        val bookmarkBtn = v.findViewById<ImageButton>(R.id.cif_bookmark_btn)

        val dbResultImage = dbHelper.getResultImageByImageLink(Uri.parse(resultImage.image_link))

        // Set checked bookmark icon.
        setCorrectBookmarkIcon(dbResultImage, bookmarkBtn)

        bookmarkBtn.setOnClickListener {
            bookmarkBtnClicked(dbResultImage, dbHelper, origImgUri, resultImage, bookmarkBtn)
        }
        v.findViewById<ImageButton>(R.id.cif_web_btn).setOnClickListener {
            webBtnClicked(resultImage)
        }

        return v
    }

    private fun setCorrectBookmarkIcon(
        dbResultImage: DBResultImage?,
        bookmarkBtn: ImageButton
    ) {
        if (dbResultImage != null) {
            bookmarkBtn.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_bookmark_solid
                )
            )
        }
    }

    private fun webBtnClicked(resultImage: ResultImage) {
        resultImage.store_link?.let { it1 -> toUrl(requireContext(), it1) } ?: run {
            Toast.makeText(requireContext(), "Unable to get URL", Toast.LENGTH_SHORT).show()
        }
    }

    private fun bookmarkBtnClicked(
        dbResultImage: DBResultImage?,
        dbHelper: DataBaseHelper,
        origImgUri: Uri?,
        resultImage: ResultImage,
        bookmarkBtn: ImageButton
    ) {
        if (dbResultImage == null) {
            val originalImage = originalImageExists(dbHelper, origImgUri)

            val resultImages = arrayListOf<ResultImage?>()
            resultImages.add(resultImage)

            val dbResultImages = convertFormat(originalImage, resultImages)

            if (!dbResultImages.isNullOrEmpty()) {
                dbHelper.putResultImages(dbResultImages)
                bookmarkBtn.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_bookmark_solid
                    )
                )
            }
        } else {
            val selectedImage =
                dbHelper.getResultImageByImageLink(Uri.parse(resultImage.image_link))
            val origId = selectedImage?.originalImgID

            dbHelper.deleteResultImageByUri(resultImage.image_link.toString())
            bookmarkBtn.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_bookmark_stroke
                )
            )

            if (origId != null) {
                val results = dbHelper.getListOfResultsById(origId)
                if (results.isNullOrEmpty()) {
                    dbHelper.deleteOriginalAndResults(origId)
                }
            }
        }
    }

    private fun convertFormat(
        originalImage: DBOriginalImage?,
        resultImages: ArrayList<ResultImage?>
    ): ArrayList<DBResultImage>? {
        return originalImage?.id?.let { ogi ->
            Globals.convertResultImagesToDBModel(resultImages, ogi)
        }
    }

    private fun originalImageExists(
        dbHelper: DataBaseHelper,
        origImgUri: Uri?
    ): DBOriginalImage? {
        if (dbHelper.getOriginalImageByUri(origImgUri.toString()) == null) {
            dbHelper.putOriginalImage(
                DBOriginalImage(
                    null, origImgUri,
                    Calendar.getInstance().time.toString()
                )
            )
        }

        return dbHelper.getOriginalImageByUri(origImgUri.toString())
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