package no.kristiania.prg208_1_exam.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.widget.TextView
import androidx.fragment.app.Fragment
import no.kristiania.prg208_1_exam.R
import org.w3c.dom.Text

class LoadingDialog(private val activity: Activity): Fragment() {

    private lateinit var dialog: AlertDialog

    fun startLoadingDialog(): TextView {
        val builder = AlertDialog.Builder(activity)

        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_dialog, null))
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()

        return dialog.findViewById(R.id.loading_dialog_text_view)
    }

    fun endLoadingDialog() {
        dialog.dismiss()
    }

}