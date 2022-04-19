package no.kristiania.prg208_1_exam

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.jacksonandroidnetworking.JacksonParserFactory
import no.kristiania.prg208_1_exam.adapters.ImageAdapter
import no.kristiania.prg208_1_exam.fragments.ChosenImageFragment
import no.kristiania.prg208_1_exam.models.ResultImage
import no.kristiania.prg208_1_exam.repository.ImageRepo
import no.kristiania.prg208_1_exam.services.APIService
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import kotlin.math.log


class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fragmentManager: FragmentManager
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initializeAndroidNetworking()

        fragmentManager = supportFragmentManager

        Globals.setHeaderFragment(fragmentManager)
        overridePendingTransition(0, 0)

        val imageRepo = ImageRepo()
        val viewModelFactory = SearchViewModelFactory(imageRepo)
        viewModel = ViewModelProvider(this, viewModelFactory)[SearchViewModel::class.java]

        val bundle: Bundle? = intent.extras
        val imageUri: Uri? = bundle?.getParcelable("chosenImageUri")

        uploadImageToServer(imageUri)

        viewModel.postResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {
                onSuccessfulResponse(response, imageUri)
            } else {
                onErrorResponse(response)
            }
        })
    }

    private fun initializeAndroidNetworking() {
        AndroidNetworking.initialize(getApplicationContext())
        AndroidNetworking.setParserFactory(JacksonParserFactory())
    }

    private fun setRecyclerView(results: ArrayList<ResultImage>): ImageAdapter {
        val adapter = ImageAdapter(results)
        recyclerView = findViewById(R.id.s_results_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = adapter
        return adapter
    }

    private fun uploadImageToServer(imageUri: Uri?) {
        if (imageUri != null) {
            /*val requestName = "image"
            val file = File(getPathFromURI(imageUri))

            val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData(requestName, file.name, requestFile)
            val fullName = requestName.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            viewModel.postImage(body, fullName)*/
            val file = File(getPathFromURI(imageUri))
            APIService().postImage(file)
        }
    }

    private fun onErrorResponse(response: Response<String>) {
        Log.d(
            "Response",
            "response is not successful" + response.code() + "\n" + response.message()
        )
        //Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
    }

    private fun onSuccessfulResponse(response: Response<String>, uri: Uri?) {
        Log.d("Response", "Response = Success!")
        Log.d("Response", response.body().toString())
        Log.d("Response", response.code().toString())
        Log.d("Response", response.message())

        val originalImage = findViewById<ImageView>(R.id.s_orig_img)
        originalImage.setImageURI(uri)

        viewModel.getImage("bing", response.body().toString())

        viewModel.getResponse.observe(this, Observer { res ->
            val results = res.body() as ArrayList<ResultImage>
            val adapter = setRecyclerView(results)

            val chosenImageFragment = ChosenImageFragment()
            adapter.setOnItemClickListener(object : ImageAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    Toast.makeText(applicationContext, "Button $position pressed", Toast.LENGTH_SHORT).show()
                    switchFragment(chosenImageFragment)
                }
            })
        })
    }

    private fun getPathFromURI(uri: Uri?): String? {
        val cursor: Cursor? = uri?.let { contentResolver.query(it, null, null, null, null) }
        cursor?.moveToFirst()
        val idx: Int? = cursor?.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return idx?.let { cursor?.getString(it) }
    }

    private fun switchFragment(fragment: Fragment) {
        fragmentManager.beginTransaction()
            .replace(R.id.se_content_layout, fragment, "content_fragment").commit()
    }
}