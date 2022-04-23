package no.kristiania.prg208_1_exam.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import no.kristiania.prg208_1_exam.models.AllSearches
import no.kristiania.prg208_1_exam.R
import no.kristiania.prg208_1_exam.models.SearchItem

class MainRecyclerAdapter(
    private val context: Context,
    private val allSearchesList: List<AllSearches>
) :
    RecyclerView.Adapter<MainRecyclerAdapter.MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.main_recycler_row_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.categoryTitle.text = allSearchesList[position].searchTitle
        setSearchItemRecycler(holder.itemRecycler, allSearchesList[position].searchItemList)
    }

    override fun getItemCount(): Int {
        return allSearchesList.size
    }

    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var categoryTitle: TextView = itemView.findViewById(R.id.row_title)
        var itemRecycler: RecyclerView = itemView.findViewById(R.id.item_recycler)

    }

    private fun setSearchItemRecycler(
        recyclerView: RecyclerView,
        categoryItemList: List<SearchItem>
    ) {
        val itemRecyclerAdapter = CategoryItemRecyclerAdapter(context, categoryItemList)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = itemRecyclerAdapter
    }

}