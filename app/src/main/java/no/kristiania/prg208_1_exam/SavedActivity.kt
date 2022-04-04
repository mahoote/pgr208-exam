package no.kristiania.prg208_1_exam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SavedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)

        Globals.setHeaderFragment(supportFragmentManager)
        overridePendingTransition(0, 0)
    }
}