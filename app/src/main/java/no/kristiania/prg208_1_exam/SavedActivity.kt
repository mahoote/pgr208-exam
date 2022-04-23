package no.kristiania.prg208_1_exam

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import no.kristiania.prg208_1_exam.adapters.MainRecyclerAdapter
import no.kristiania.prg208_1_exam.db.DataBaseHelper
import no.kristiania.prg208_1_exam.fragments.NoSavedResultsFragment
import no.kristiania.prg208_1_exam.models.AllSearches
import no.kristiania.prg208_1_exam.models.SearchItem

class SavedActivity : AppCompatActivity() {
    private var mainCategoryRecycler: RecyclerView? = null
    private var mainRecyclerAdapter: MainRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)

        Globals.setHeaderFragment(supportFragmentManager)
        overridePendingTransition(0, 0)

        val dbHelper = DataBaseHelper(applicationContext)
        val allOriginalImages = dbHelper.getAllOriginalImages()
        val allSearchesList: MutableList<AllSearches> = ArrayList()

        if(allOriginalImages.size > 0) {

            allOriginalImages.forEach { o ->
                val list = o.id?.let { dbHelper.getListOfResultsAsSearchItemById(it) }
                o.uri?.let { o.id?.let { it1 -> SearchItem(it1, it, true) } }?.let { list?.add(0, it) }
                list?.let { AllSearches(o.created.toString(), it) }?.let { allSearchesList.add(it) }
            }
            setMainRecycler(allSearchesList)

        } else {
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                .add(R.id.sa_content_fragment_container, NoSavedResultsFragment(), "content-fragment").commit()
        }
    }

    private fun setMainRecycler(allSearchesList: List<AllSearches>) {
        mainCategoryRecycler = findViewById(R.id.main_recycler)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        mainCategoryRecycler?.layoutManager = layoutManager
        mainRecyclerAdapter = MainRecyclerAdapter(this, allSearchesList)
        mainCategoryRecycler?.adapter = mainRecyclerAdapter
    }
}