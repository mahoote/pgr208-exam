package no.kristiania.prg208_1_exam.view

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import no.kristiania.prg208_1_exam.R
import no.kristiania.prg208_1_exam.controller.utils.Utils
import no.kristiania.prg208_1_exam.controller.adapters.SavedVerticalAdapter
import no.kristiania.prg208_1_exam.view.fragments.ChosenImageFragment
import no.kristiania.prg208_1_exam.view.fragments.OnDataPass
import no.kristiania.prg208_1_exam.controller.service.DatabaseService
import no.kristiania.prg208_1_exam.model.models.AllSearches
import no.kristiania.prg208_1_exam.model.models.DBOriginalImage
import no.kristiania.prg208_1_exam.model.models.DBResultImage

class SavedActivity : AppCompatActivity(), OnDataPass {

    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter: SavedVerticalAdapter
    private lateinit var fragmentManager: FragmentManager
    private var allSearchesList: ArrayList<AllSearches> = ArrayList()

    // If loading screen is used.
    /*private lateinit var shimmerFrameLayout: ShimmerFrameLayout*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)

        fragmentManager = supportFragmentManager

        Utils.setHeaderFragment(supportFragmentManager)
        overridePendingTransition(0, 0)

        val dbService = DatabaseService(this)
        val allOriginalImages = dbService.getAllOriginalImages()

        // If loading screen is used.
        /*shimmerFrameLayout = findViewById(R.id.sa_shimmer_layout)
        shimmerFrameLayout.startShimmer()
        val mainLooper = Looper.getMainLooper()*/

        if (allOriginalImages.size > 0) {

            Thread {
                createAllSearchesList(allOriginalImages, dbService)
                initRecyclerView()

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
    ) {
        // Make sure list is empty.
        allSearchesList = ArrayList()

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

            if(dbResultImages?.size != 0) {
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

        if (allSearchesList.size == 0) {
            showEmptyFragment()
        }
    }

    private fun showEmptyFragment() {
        val fragmentManager = supportFragmentManager
        val container: Int = R.id.sa_content_fragment_container
        val frameLayout = findViewById<FrameLayout>(container)
        Utils.showEmptyView(frameLayout, container, fragmentManager)
    }

    private fun initRecyclerView() {
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
        val contentFragment = fragmentManager.findFragmentByTag("content_fragment")

        if (contentFragment != null) {
            fragmentManager.beginTransaction().remove(contentFragment).commit()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
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