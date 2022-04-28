package no.kristiania.prg208_1_exam

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import no.kristiania.prg208_1_exam.adapters.SavedVerticalAdapter
import no.kristiania.prg208_1_exam.fragments.ChosenImageFragment
import no.kristiania.prg208_1_exam.fragments.OnDataPass
import no.kristiania.prg208_1_exam.model.service.DatabaseService
import no.kristiania.prg208_1_exam.models.AllSearches
import no.kristiania.prg208_1_exam.models.DBOriginalImage
import no.kristiania.prg208_1_exam.models.DBResultImage

class SavedActivity : AppCompatActivity(), OnDataPass {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: SavedVerticalAdapter

    // If loading screen is used.
    /*private lateinit var shimmerFrameLayout: ShimmerFrameLayout*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)

        Utils.setHeaderFragment(supportFragmentManager)
        overridePendingTransition(0, 0)

        val dbService = DatabaseService(this)
        val allOriginalImages = dbService.getAllOriginalImages()
        val allSearchesList: ArrayList<AllSearches> = ArrayList()

        // If loading screen is used.
        /*shimmerFrameLayout = findViewById(R.id.sa_shimmer_layout)
        shimmerFrameLayout.startShimmer()
        val mainLooper = Looper.getMainLooper()*/

        if (allOriginalImages.size > 0) {

            Thread {
                createAllSearchesList(allOriginalImages, dbService, allSearchesList)
                initRecyclerView(allSearchesList)

                // If loading screen is used.
                /*Handler(mainLooper).post {
                    stopLoadingScreen()
                }*/
            }.start()

        } else {
            showEmptyFragment()
        }
    }

    private fun createAllSearchesList(
        allOriginalImages: ArrayList<DBOriginalImage>,
        dbService: DatabaseService,
        allSearchesList: ArrayList<AllSearches>
    ) {
        allOriginalImages.forEach { originalImage ->
            val dbResultImages =
                originalImage.id?.let { origId -> dbService.getListOfResultsById(origId) }
            val originalDbResultImage =
                originalImage.id?.let {
                    DBResultImage(
                        null, null, null,
                        null, null, null,
                        null, null, null, originalImage.byteArray, null, it
                    )
                }

            originalDbResultImage?.let { dbResultImages?.add(0, it) }
            dbResultImages?.let {
                allSearchesList.add(
                    AllSearches(
                        originalImage.created.toString(),
                        it
                    )
                )
            }
        }
    }

    private fun showEmptyFragment() {
        val fragmentManager = supportFragmentManager
        val container: Int = R.id.sa_content_fragment_container
        val frameLayout = findViewById<FrameLayout>(container)
        Utils.showEmptyView(frameLayout, container, fragmentManager)
    }

    private fun initRecyclerView(allSearchesList: ArrayList<AllSearches>) {
        recyclerView = findViewById(R.id.sa_recycler_view)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@SavedActivity)
            val decoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
            val chosenImageFragment = ChosenImageFragment()
            recyclerViewAdapter = SavedVerticalAdapter(this@SavedActivity, chosenImageFragment, allSearchesList)
            adapter = recyclerViewAdapter
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    // If loading screen is used.
    /*private fun stopLoadingScreen() {
        shimmerFrameLayout.stopShimmer()
        shimmerFrameLayout.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }*/

    override fun onDataPass(data: Pair<Int, DBResultImage>) {
        finish()
        startActivity(intent)
    }
}