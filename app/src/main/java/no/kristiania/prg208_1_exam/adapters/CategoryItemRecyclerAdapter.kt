package com.rajendra.nestedrecyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.rajendra.nestedrecyclerview.model.CategoryItem
import no.kristiania.prg208_1_exam.R

class CategoryItemRecyclerAdapter(
    private val context: Context,
    categoryItemList: List<CategoryItem>
) :
    RecyclerView.Adapter<CategoryItemRecyclerAdapter.CategoryItemViewHolder>() {
    private val categoryItemList: List<CategoryItem>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        return CategoryItemViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.category_row_items, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        holder.itemImage.setImageResource(categoryItemList[position].imageUrl)
    }

    override fun getItemCount(): Int {
        return categoryItemList.size
    }

    class CategoryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView

        init {
            itemImage = itemView.findViewById(R.id.item_image)
        }
    }

    init {
        this.categoryItemList = categoryItemList
    }
}