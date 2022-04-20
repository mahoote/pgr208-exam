package no.kristiania.prg208_1_exam

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.jacksonandroidnetworking.JacksonParserFactory
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import no.kristiania.prg208_1_exam.Globals.setImage
import no.kristiania.prg208_1_exam.services.APIService
import java.io.File
import java.lang.Exception


class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initializeAndroidNetworking()

        fragmentManager = supportFragmentManager

        Globals.setHeaderFragment(fragmentManager)
        overridePendingTransition(0, 0)

        val bundle: Bundle? = intent.extras
        val imageUri: Uri? = bundle?.getParcelable("chosenImageUri")

        uploadImageToServer(imageUri)

    }

    private fun initializeAndroidNetworking() {
        AndroidNetworking.initialize(getApplicationContext())
        AndroidNetworking.setParserFactory(JacksonParserFactory())
    }

    /*private fun setRecyclerView(results: ArrayList<ResultImage>): ImageAdapter {
        val adapter = ImageAdapter(results)
        recyclerView = findViewById(R.id.s_results_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = adapter
        return adapter
    }*/

    private fun uploadImageToServer(imageUri: Uri?) {
        if (imageUri != null) {
            val file = File(getPathFromURI(imageUri)!!)
            APIService().postImage(this, file)
        }
    }

    fun onErrorResponse(anError: ANError) {
        Log.d("Response", "An error occured")
        anError.printStackTrace()
        //Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
    }

    fun onSuccessfulResponse(response: String) {
        Log.d("Response", "Response = Success!")
        Log.d("Response", "After api: $response")

        val originalImage = findViewById<ImageView>(R.id.s_orig_img)
        val imgTxtStatus = findViewById<TextView>(R.id.s_orig_img_status_txt)

        setImage("response", originalImage, imgTxtStatus)


        /*viewModel.getImage("bing", response.body.toString())

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
        })*/
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