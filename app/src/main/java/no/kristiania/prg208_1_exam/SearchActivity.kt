package no.kristiania.prg208_1_exam

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
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
    private lateinit var results: ArrayList<ResultImage>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        fragmentManager = supportFragmentManager

        Globals.setHeaderFragment(fragmentManager)
        overridePendingTransition(0, 0)

        val bundle: Bundle? = intent.extras

        if(bundle != null) {
            val chosenImageUri = bundle!!.getString("chosenImageUri")
            results = bundle.getSerializable("results") as ArrayList<ResultImage>

            Log.d("m_debug", "$results")

            adapter = setRecyclerView(results)
            setOriginalImage(chosenImageUri)
        } else {

            var latestCache: Map.Entry<String, CachedImages>? = null
            if(Globals.cachedImages.isNotEmpty()) {
                Globals.cachedImages.forEach { cachedImage ->
                    latestCache?.let {
                        if(it.value.created < cachedImage.value.created)
                            latestCache = cachedImage
                    } ?: run {
                        latestCache = cachedImage
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
                    replaceFragment(chosenImageFragment, position)
                }
            })
        }
    }

    private fun setRecyclerView(results: ArrayList<ResultImage>): ImageAdapter {
        val adapter = ImageAdapter(results)
        recyclerView = findViewById(R.id.s_results_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
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

    private fun replaceFragment(fragment: Fragment, position: Int) {
        if(::results.isInitialized) {
            val bundle = Bundle()
            bundle.putSerializable("resultImage", results[position])
            fragment.arguments = bundle

            fragmentManager.beginTransaction()
                .replace(R.id.se_content_layout, fragment, "content_fragment").commit()
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}