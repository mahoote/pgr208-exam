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
import no.kristiania.prg208_1_exam.adapters.SavedCategoryAdapter
import no.kristiania.prg208_1_exam.model.service.DatabaseService
import no.kristiania.prg208_1_exam.models.AllSearches
import no.kristiania.prg208_1_exam.models.DBResultImage

class SavedActivity : AppCompatActivity() {
    private lateinit var mainCategoryRecycler: RecyclerView
    private lateinit var categoryAdapter: SavedCategoryAdapter
    private lateinit var shimmerFrameLayout: ShimmerFrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)

        Globals.setHeaderFragment(supportFragmentManager)
        overridePendingTransition(0, 0)

        val dbService = DatabaseService(this)
        val allOriginalImages = dbService.getAllOriginalImages()
        val allSearchesList: MutableList<AllSearches> = ArrayList()

        shimmerFrameLayout = findViewById(R.id.sa_shimmer_layout)
        shimmerFrameLayout.startShimmer()


        if (allOriginalImages.size > 0) {

            val mainLooper = Looper.getMainLooper()

            Thread {
                allOriginalImages.forEach { originalImage ->
                    val dbResultImages =
                        originalImage.id?.let { origId -> dbService.getListOfResultsById(origId) }
                    val originalDbResultImage =
                        originalImage.id?.let {
                            DBResultImage(null, null, null,
                                null, null,null,
                                null, null, null, originalImage.byteArray, null, it
                            )}

                    originalDbResultImage?.let { dbResultImages?.add(0, it) }
                    dbResultImages?.let { allSearchesList.add(AllSearches(originalImage.created.toString(), it)) }
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
        mainCategoryRecycler.layoutManager = layoutManager
        categoryAdapter =
            SavedCategoryAdapter(this, allSearchesList)
        mainCategoryRecycler.adapter = categoryAdapter
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun stopLoadingScreen() {
        shimmerFrameLayout.stopShimmer()
        shimmerFrameLayout.visibility = View.GONE
        mainCategoryRecycler.visibility = View.VISIBLE
    }
}