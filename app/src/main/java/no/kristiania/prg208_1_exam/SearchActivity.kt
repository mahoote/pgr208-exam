package no.kristiania.prg208_1_exam

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import no.kristiania.prg208_1_exam.adapters.ImageAdapter
import no.kristiania.prg208_1_exam.fragments.ChosenImageFragment
import no.kristiania.prg208_1_exam.models.ResultImage
import no.kristiania.prg208_1_exam.repository.Repository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File


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

            val file = File(getRealPathFromURI(imageUri)!!)
            val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
            val fullName = "image".toRequestBody("multipart/form-data".toMediaTypeOrNull())
            viewModel.postImage(body, fullName)
        }
        viewModel.postResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {
                Log.d("Response", "Response = Success!")
                Log.d("Response", response.body().toString())
                Log.d("Response", response.code().toString())
                Log.d("Response", response.message())
            } else {
                Log.d("Response", "response is not successful" + response.code() + "\n" + response.message())
              //Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
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

    private fun getRealPathFromURI(uri: Uri?): String? {
        val cursor: Cursor? = uri?.let { contentResolver.query(it, null, null, null, null) }
        cursor?.moveToFirst()
        val idx: Int? = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return idx?.let { cursor.getString(it) }
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