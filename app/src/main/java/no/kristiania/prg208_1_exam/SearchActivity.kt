package no.kristiania.prg208_1_exam

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import no.kristiania.prg208_1_exam.adapters.ImageAdapter
import no.kristiania.prg208_1_exam.models.ResultImage

class SearchActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private var results: ArrayList<ResultImage> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        Globals.setHeaderFragment(supportFragmentManager)
        overridePendingTransition(0, 0)

        addImages(10)

        val adapter = ImageAdapter(results)
        recyclerView = findViewById(R.id.s_results_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : ImageAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(applicationContext, "Button $position pressed", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun addImages(amount: Int) {
        for (i in 0..amount) {
            results.add(ResultImage(R.drawable.ic_launcher_background, "Test"))
        }
    }
}