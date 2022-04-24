package no.kristiania.prg208_1_exam

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import no.kristiania.prg208_1_exam.adapters.MainRecyclerAdapter
import no.kristiania.prg208_1_exam.db.DataBaseHelper
import no.kristiania.prg208_1_exam.models.AllSearches
import no.kristiania.prg208_1_exam.models.SearchItem

class SavedActivity : AppCompatActivity() {
    private var mainCategoryRecycler: RecyclerView? = null
    private var mainRecyclerAdapter: MainRecyclerAdapter? = null
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)

        Globals.setHeaderFragment(supportFragmentManager)
        overridePendingTransition(0, 0)

        val dbHelper = DataBaseHelper(applicationContext)
        val allOriginalImages = dbHelper.getAllOriginalImages()
        val allSearchesList: MutableList<AllSearches> = ArrayList()

        shimmerFrameLayout = findViewById(R.id.sa_shimmer_layout)
        shimmerFrameLayout.startShimmer()


        if(allOriginalImages.size > 0) {

            val mainLooper = Looper.getMainLooper()

            Thread {
                allOriginalImages.forEach { o ->
                    val list = o.id?.let { dbHelper.getListOfResultsAsSearchItemById(it) }
                    o.uri?.let { o.id?.let { it1 -> SearchItem(it1, it, true) } }
                        ?.let { list?.add(0, it) }
                    list?.let { AllSearches(o.created.toString(), it) }
                        ?.let { allSearchesList.add(it) }
                }
                setMainRecycler(allSearchesList)

                Handler(mainLooper).post {
                    stopLoadingScreen()
                }
            }.start()

        } else {
            val fragmentManager = supportFragmentManager
            val container: Int = R.id.sa_content_fragment_container
            val frameLayout = findViewById<FrameLayout>(container)
            Globals.showEmptyView(frameLayout, container, fragmentManager)
        }

    }

    private fun setMainRecycler(allSearchesList: List<AllSearches>) {
        mainCategoryRecycler = findViewById(R.id.main_recycler)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        mainCategoryRecycler?.layoutManager = layoutManager
        mainRecyclerAdapter = MainRecyclerAdapter(this, allSearchesList, shimmerFrameLayout, mainCategoryRecycler)
        mainCategoryRecycler?.adapter = mainRecyclerAdapter
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun stopLoadingScreen() {
        shimmerFrameLayout.stopShimmer()
        shimmerFrameLayout.visibility = View.GONE
        mainCategoryRecycler?.visibility = View.VISIBLE
    }
}