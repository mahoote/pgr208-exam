package no.kristiania.prg208_1_exam.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.squareup.picasso.Picasso
import no.kristiania.prg208_1_exam.Globals.toDp
import no.kristiania.prg208_1_exam.R
import no.kristiania.prg208_1_exam.models.ResultImage

class ChosenImageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v =  inflater.inflate(R.layout.fragment_chosen_image, container, false)

        v.findViewById<ImageButton>(R.id.cif_close_btn).setOnClickListener {
            Log.i("debug", "Close")
            parentFragmentManager.beginTransaction().remove(this).commit()
        }

        val resultImage = arguments?.getSerializable("resultImage") as ResultImage

        val imageView = v.findViewById<ImageView>(R.id.cif_chosen_img)
        val nameView = v.findViewById<TextView>(R.id.cif_img_name_txt)
        val descView = v.findViewById<TextView>(R.id.cif_img_desc_view)

        Picasso.get().load(resultImage.image_link).into(imageView)

        imageView.post {
            sizeCheck(imageView)
        }

        nameView.text = resultImage.name
        descView.text = resultImage.description

        v.findViewById<ImageButton>(R.id.cif_bookmark_btn).setOnClickListener {
            // TODO: Save image to DB if not checked, remove from DB if checked
            Toast.makeText(requireContext(), "Save", Toast.LENGTH_SHORT).show()
        }
        v.findViewById<ImageButton>(R.id.cif_web_btn).setOnClickListener {
            // TODO: Take to website.
            Toast.makeText(requireContext(), "To website", Toast.LENGTH_SHORT).show()
        }

        return v
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