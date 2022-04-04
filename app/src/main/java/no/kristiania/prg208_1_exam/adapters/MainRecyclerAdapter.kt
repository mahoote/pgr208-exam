package com.rajendra.nestedrecyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rajendra.nestedrecyclerview.model.AllCategory
import com.rajendra.nestedrecyclerview.model.CategoryItem
import no.kristiania.prg208_1_exam.R

class MainRecyclerAdapter(private val context: Context, allCategoryList: List<AllCategory>) :
    RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>() {
    private val allCategoryList: List<AllCategory>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.main_recycler_row_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.categoryTitle.setText(allCategoryList[position].categoryTitle)
        setCatItemRecycler(holder.itemRecycler, allCategoryList[position].categoryItemList)
    }

    override fun getItemCount(): Int {
        return allCategoryList.size
    }

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryTitle: TextView
        var itemRecycler: RecyclerView

        init {
            categoryTitle = itemView.findViewById(R.id.row_title)
            itemRecycler = itemView.findViewById(R.id.item_recycler)
        }
    }

    private fun setCatItemRecycler(
        recyclerView: RecyclerView,
        categoryItemList: List<CategoryItem>
    ) {
        val itemRecyclerAdapter = CategoryItemRecyclerAdapter(context, categoryItemList)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = itemRecyclerAdapter
    }

    init {
        this.allCategoryList = allCategoryList
    }
}