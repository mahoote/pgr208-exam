package no.kristiania.prg208_1_exam.fragments

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import no.kristiania.prg208_1_exam.MainActivity
import no.kristiania.prg208_1_exam.R

class HeaderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_header, container, false)

        highlightCurrentActivity(view)
        newImageOnClick(view)

        return view
    }

    private fun newImageOnClick(view: View) {
        view.findViewById<AppCompatButton>(R.id.hf_new_image_btn).setOnClickListener {
            Toast.makeText(context, "Click new image", Toast.LENGTH_SHORT).show()
            val fragmentManager = parentFragmentManager
            val contentFragment = fragmentManager.findFragmentByTag("content_fragment")

            if (contentFragment != null) {
                fragmentManager.beginTransaction().remove(contentFragment).commit()
            } else {
                val intent = Intent(this.requireContext(), MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun highlightCurrentActivity(v: View) {
        val currentActivity = getCurrentActivity()
        if(currentActivity != null) {
            val blueBtn: Drawable? = context?.let { ContextCompat.getDrawable(it, R.drawable.nav_blue_btn) }
            when {
                currentActivity.contains("MainActivity") -> {
                    v.findViewById<AppCompatButton>(R.id.hf_new_image_btn).background = blueBtn
                }
            }
        }
    }

    private fun getCurrentActivity():String? {
        val activityManager = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        return activityManager.getRunningTasks(1)[0].topActivity?.className
    }

}