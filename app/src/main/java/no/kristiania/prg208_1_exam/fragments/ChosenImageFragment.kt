package no.kristiania.prg208_1_exam.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.Toast
import com.revosleap.blurrylayout.BlurryLayout
import no.kristiania.prg208_1_exam.R

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

        return v
    }


}