package no.kristiania.prg208_1_exam

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario.launch
import com.androidnetworking.AndroidNetworking.post
import com.facebook.shimmer.ShimmerFrameLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import no.kristiania.prg208_1_exam.adapters.MainRecyclerAdapter
import no.kristiania.prg208_1_exam.db.DataBaseHelper
import no.kristiania.prg208_1_exam.fragments.NoSavedResultsFragment
import no.kristiania.prg208_1_exam.models.AllSearches
import no.kristiania.prg208_1_exam.models.SearchItem
import okhttp3.internal.concurrent.Task

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
                    done()
                }
            }.start()

        } else {
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                .add(R.id.sa_content_fragment_container, NoSavedResultsFragment(), "content-fragment").commit()
        }

//        shimmerFrameLayout.visibility = View.GONE
//        mainCategoryRecycler?.visibility = View.VISIBLE

//        Handler().postDelayed( {
//            shimmerFrameLayout.stopShimmer()
//            shimmerFrameLayout.visibility = View.GONE
//            mainCategoryRecycler?.visibility = View.VISIBLE
//        }, 3000)


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

    private fun done() {
        shimmerFrameLayout.stopShimmer()
        shimmerFrameLayout.visibility = View.GONE
        mainCategoryRecycler?.visibility = View.VISIBLE
    }
}