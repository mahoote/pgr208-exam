package no.kristiania.prg208_1_exam

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rajendra.nestedrecyclerview.adapter.MainRecyclerAdapter
import com.rajendra.nestedrecyclerview.model.AllCategory
import com.rajendra.nestedrecyclerview.model.CategoryItem

class SavedActivity : AppCompatActivity() {
    var mainCategoryRecycler: RecyclerView? = null
    var mainRecyclerAdapter: MainRecyclerAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)

        Globals.setHeaderFragment(supportFragmentManager)

        // here we will add some dummy data to our model class

        // here we will add data to category item model class

        // added in first category
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
        setMainCategoryRecycler(allCategoryList)
    }

    private fun setMainCategoryRecycler(allCategoryList: List<AllCategory>) {
        mainCategoryRecycler = findViewById(R.id.main_recycler)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        mainCategoryRecycler?.layoutManager = layoutManager
        mainRecyclerAdapter = MainRecyclerAdapter(this, allCategoryList)
        mainCategoryRecycler?.adapter = mainRecyclerAdapter
    } // Hi today we are gonna make a nested recyclerview
    // one is horizontal and 2nd is vertical and we will place then together.
    // before getting started make sure to subscribe and hit the bell icon to get update when i post video.
    // so 1st we will setup verticle recyclerview.
    // so now we will setup a horizontal recyclerview. which having category elements
    // now lets goto the all category Model
    // similarly u can add many types
    // so this tutorial has been completed if u have any question and doubt plz comment
    // see you in the next tutorial
}