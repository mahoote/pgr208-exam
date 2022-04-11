package no.kristiania.prg208_1_exam

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import no.kristiania.prg208_1_exam.adapters.ImageAdapter
import no.kristiania.prg208_1_exam.fragments.ChosenImageFragment
import no.kristiania.prg208_1_exam.models.Post
import no.kristiania.prg208_1_exam.models.ResultImage
import no.kristiania.prg208_1_exam.repository.Repository

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fragmentManager: FragmentManager
    private lateinit var viewModel: SearchViewModel

    private var results: ArrayList<ResultImage> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        fragmentManager = supportFragmentManager

        Globals.setHeaderFragment(fragmentManager)
        overridePendingTransition(0, 0)

        addImages(10)

        val adapter = ImageAdapter(results)
        recyclerView = findViewById(R.id.s_results_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = adapter


        val chosenImageFragment = ChosenImageFragment()
        adapter.setOnItemClickListener(object : ImageAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(applicationContext, "Button $position pressed", Toast.LENGTH_SHORT).show()
                switchFragment(chosenImageFragment)
            }

        })

        val repository = Repository()
        val viewModelFactory = SearchViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]

        // TODO: POST request:

        val bundle: Bundle? = intent.extras

        val imageUri: Uri? = bundle?.getParcelable("chosenImageUri")
        if (imageUri != null) {
            viewModel.postImage(imageUri)
        }
        viewModel.postResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {
                Log.d("Response", response.body().toString())
                Log.d("Response", response.code().toString())
                Log.d("Response", response.message())
            } else {
              Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
            }
        })

        // TODO: GET request:
//        viewModel.getAllPosts()
//        viewModel.allPosts.observe(this, Observer { response ->
//            if(response.isSuccessful) {
//                response.body()?.forEach {
//                    Log.d("Response", it.userId.toString())
//                    Log.d("Response", it.id.toString())
//                    Log.d("Response", it.title)
//                    Log.d("Response", it.body)
//                }
//            } else {
//                Log.d("Response", response.errorBody().toString())
//            }
//        })

    }

    private fun addImages(amount: Int) {
        for (i in 0..amount) {
            results.add(ResultImage(R.drawable.ic_launcher_background, "Test"))
        }
    }

    private fun switchFragment(fragment: Fragment) {
        fragmentManager.beginTransaction()
            .replace(R.id.se_content_layout, fragment, "content_fragment").commit()
    }
}