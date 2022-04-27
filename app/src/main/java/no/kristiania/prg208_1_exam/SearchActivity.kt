package no.kristiania.prg208_1_exam

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import no.kristiania.prg208_1_exam.Globals.loadImage
import no.kristiania.prg208_1_exam.adapters.SearchImageAdapter
import no.kristiania.prg208_1_exam.fragments.ChosenImageFragment
import no.kristiania.prg208_1_exam.model.service.DatabaseService
import no.kristiania.prg208_1_exam.models.DBOriginalImage
import no.kristiania.prg208_1_exam.models.DBResultImage
import no.kristiania.prg208_1_exam.models.ResultImage
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var fragmentManager: FragmentManager
    private lateinit var adapter: SearchImageAdapter
    private lateinit var results: ArrayList<DBResultImage>
    private lateinit var uriString: String
    private lateinit var origBitmap: Bitmap
    private lateinit var dbService: DatabaseService
    private var origDBImageId: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        fragmentManager = supportFragmentManager

        Globals.setHeaderFragment(fragmentManager)
        overridePendingTransition(0, 0)

        dbService = DatabaseService(this)

        // TODO: DEBUG!!
        dbService.clear()

        val bundle: Bundle? = intent.extras

        if(bundle != null) {
            results = arrayListOf()
            uriString = bundle.getString("uploadedUri")!!
            val resultImages = bundle.getSerializable("results") as ArrayList<ResultImage>

            resultImages.forEach { resultImage ->
                val dbResultImage = Globals.convertResultImageToDBModelNoOriginal(resultImage)
                results.add(dbResultImage)
            }

            Log.d("m_debug", "$results")

            adapter = setRecyclerView(results)
//            val origImageView = setOriginalImage(uriString)
//
//            origImageView.post {
//                origBitmap = Globals.drawableToBitmap(origImageView.drawable as BitmapDrawable)
//            }
            // TODO: Use method to set image.
            val originalImage = findViewById<ImageView>(R.id.s_orig_img)
            val imgTxtStatus = findViewById<TextView>(R.id.s_orig_img_status_txt)

            Log.d("db_debug", "onCreate: Setting image...")

            if (imgTxtStatus != null) {
                Log.d("db_debug", "onCreate: Img status is not null")

                loadImage(uriString, originalImage, imgTxtStatus,
                    object :
                        Callback {
                        override fun onSuccess() {
                            origBitmap = Globals.drawableToBitmap(originalImage.drawable as BitmapDrawable)
                            Log.d("db_debug", "onCreate: set orig image to bitmap: $origBitmap")

                            val byteArray = Globals.bitmapToByteArray(origBitmap)

                            origDBImageId = dbService.putOriginalImage(
                                DBOriginalImage(
                                    null, byteArray,
                                    Calendar.getInstance().time.toString()
                                )
                            )
                        }

                        override fun onError(e: Exception?) {
                            Log.e("Error", "Picasso load image: ${e?.printStackTrace()}")
                            e?.printStackTrace()
                        }
                    })
            }
        } else {

            val container: Int = R.id.se_content_fragment_container
            val frameLayout = findViewById<FrameLayout>(container)
            Globals.showEmptyView(frameLayout, container, fragmentManager)

//            var latestCache: Map.Entry<String, CachedImages>? = null
//            if(Globals.cachedImages.isNotEmpty()) {
//                Globals.cachedImages.forEach { cachedImage ->
//                    latestCache?.let {
//                        if(it.value.created < cachedImage.value.created)
//                            latestCache = cachedImage
//                    } ?: run {
//                        latestCache = cachedImage
//                    }
//                }
//                adapter = setRecyclerView(latestCache!!.value.images as ArrayList<ResultImage>)
//                setOriginalImage(latestCache!!.value.imageUri.toString())
//            }
//            else {
//                val container: Int = R.id.se_content_fragment_container
//                val frameLayout = findViewById<FrameLayout>(container)
//                Globals.showEmptyView(frameLayout, container, fragmentManager)
//            }
        }

        if(::adapter.isInitialized) {
            val chosenImageFragment = ChosenImageFragment()
            adapter.setOnItemClickListener(object : SearchImageAdapter.OnItemClickListener {
                override fun onItemClick(position: Int) {

                    val imageView = adapter.getHolders()[position].img
                    val chosenBitmapImage = Globals.drawableToBitmap(imageView.drawable as BitmapDrawable)

                    replaceFragment(chosenImageFragment, position, chosenBitmapImage, origBitmap)
                }
            })
        }
    }

    private fun setRecyclerView(results: ArrayList<DBResultImage>): SearchImageAdapter {
        val adapter = SearchImageAdapter(results)
        recyclerView = findViewById(R.id.s_results_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
        return adapter
    }

    private fun setOriginalImage(uriString: String?): ImageView {
        val originalImage = findViewById<ImageView>(R.id.s_orig_img)
        val imgTxtStatus = findViewById<TextView>(R.id.s_orig_img_status_txt)

        if (imgTxtStatus != null && uriString != null) {
            loadImage(uriString, originalImage, imgTxtStatus,
                object : Callback {
                    override fun onSuccess() {
                        imgTxtStatus.visibility = View.INVISIBLE
                    }

                    override fun onError(e: Exception?) {
                        imgTxtStatus.text = resources.getString(R.string.error_message_01)
                        Log.e("Error", "Picasso load image: ${e?.printStackTrace()}")
                        e?.printStackTrace()
                    }
                })
        }
        return originalImage
    }

    private fun replaceFragment(fragment: Fragment, position: Int, chosenBitmapImage: Bitmap, originalBitmap: Bitmap) {
        if(::results.isInitialized) {
            val bundle = Bundle()
            bundle.putParcelable("dbResultImage", results[position])
            bundle.putParcelable("chosenBitmapImage", chosenBitmapImage)
            bundle.putParcelable("originalBitmapImage", originalBitmap)
            bundle.putLong("origDBImageId", origDBImageId)

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