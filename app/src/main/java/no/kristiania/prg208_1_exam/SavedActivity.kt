package no.kristiania.prg208_1_exam

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import no.kristiania.prg208_1_exam.adapters.MainRecyclerAdapter
import no.kristiania.prg208_1_exam.models.AllCategory
import no.kristiania.prg208_1_exam.models.CategoryItem

class SavedActivity : AppCompatActivity() {
    private var mainCategoryRecycler: RecyclerView? = null
    private var mainRecyclerAdapter: MainRecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)

        Globals.setHeaderFragment(supportFragmentManager)

        val categoryItemList: MutableList<CategoryItem> = ArrayList()
        categoryItemList.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList.add(CategoryItem(1, R.drawable.ic_launcher_background))


        // added in second category
        val categoryItemList2: MutableList<CategoryItem> = ArrayList()
        categoryItemList2.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList2.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList2.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList2.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList2.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList2.add(CategoryItem(1, R.drawable.ic_launcher_background))


        // added in 3rd category
        val categoryItemList3: MutableList<CategoryItem> = ArrayList()
        categoryItemList3.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList3.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList3.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList3.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList3.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList3.add(CategoryItem(1, R.drawable.ic_launcher_background))


        // added in 4th category
        val categoryItemList4: MutableList<CategoryItem> = ArrayList()
        categoryItemList4.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList4.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList4.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList4.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList4.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList4.add(CategoryItem(1, R.drawable.ic_launcher_background))


        // added in 5th category
        val categoryItemList5: MutableList<CategoryItem> = ArrayList()
        categoryItemList5.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList5.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList5.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList5.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList5.add(CategoryItem(1, R.drawable.ic_launcher_background))
        categoryItemList5.add(CategoryItem(1, R.drawable.ic_launcher_background))

        val allCategoryList: MutableList<AllCategory> = ArrayList()
        allCategoryList.add(AllCategory("Hollywood", categoryItemList))
        allCategoryList.add(AllCategory("Best of Oscars", categoryItemList2))
        allCategoryList.add(AllCategory("Movies Dubbed in Hindi", categoryItemList3))
        allCategoryList.add(AllCategory("Category 4th", categoryItemList4))
        allCategoryList.add(AllCategory("Category 5th", categoryItemList5))
        setMainRecycler(allCategoryList)
    }

    private fun setMainRecycler(allCategoryList: List<AllCategory>) {
        mainCategoryRecycler = findViewById(R.id.main_recycler)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        mainCategoryRecycler?.layoutManager = layoutManager
        mainRecyclerAdapter = MainRecyclerAdapter(this, allCategoryList)
        mainCategoryRecycler?.adapter = mainRecyclerAdapter
    }
}