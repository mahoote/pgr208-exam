package no.kristiania.prg208_1_exam

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import no.kristiania.prg208_1_exam.Globals.loadImage
import no.kristiania.prg208_1_exam.adapters.ImageAdapter
import no.kristiania.prg208_1_exam.fragments.ChosenImageFragment
import no.kristiania.prg208_1_exam.models.CachedImages
import no.kristiania.prg208_1_exam.models.ResultImage


class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fragmentManager: FragmentManager
    private lateinit var adapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        fragmentManager = supportFragmentManager

        Globals.setHeaderFragment(fragmentManager)
        overridePendingTransition(0, 0)

        val bundle: Bundle? = intent.extras

        if(bundle != null) {
            val chosenImageUri = bundle!!.getString("chosenImageUri")
            val results = bundle.getSerializable("results")

            Log.d("m_debug", "$results")

            adapter = setRecyclerView(results as ArrayList<ResultImage>)
            setOriginalImage(chosenImageUri)
        } else {

            var latestCache: Map.Entry<String, CachedImages>? = null
            if(Globals.cachedImages.isNotEmpty()) {
                Globals.cachedImages.forEach { cache ->
                    latestCache?.let { lts ->
                        if(lts.value.created < lts.value.created)
                            latestCache = lts
                    } ?: run {
                        latestCache = cache
                    }
                }

                adapter = setRecyclerView(latestCache!!.value.images as ArrayList<ResultImage>)
                setOriginalImage(latestCache!!.value.imageUri.toString())
            }
        }

        if(::adapter.isInitialized) {
            val chosenImageFragment = ChosenImageFragment()
            adapter.setOnItemClickListener(object : ImageAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    Toast.makeText(applicationContext, "Button $position pressed", Toast.LENGTH_SHORT).show()
                    switchFragment(chosenImageFragment)
                }
            })
        }
    }

    private fun setRecyclerView(results: ArrayList<ResultImage>): ImageAdapter {
        val adapter = ImageAdapter(results)
        recyclerView = findViewById(R.id.s_results_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = adapter
        return adapter
    }

    private fun setOriginalImage(uriString: String?) {
        val originalImage = findViewById<ImageView>(R.id.s_orig_img)
        val imgTxtStatus = findViewById<TextView>(R.id.s_orig_img_status_txt)

        if (imgTxtStatus != null && uriString != null) {
            loadImage(uriString, originalImage, imgTxtStatus)
        }
    }

    private fun switchFragment(fragment: Fragment) {
        fragmentManager.beginTransaction()
            .replace(R.id.se_content_layout, fragment, "content_fragment").commit()
    }
}