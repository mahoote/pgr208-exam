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
import no.kristiania.prg208_1_exam.models.DBResultImage

class SavedCategoryAdapter(
    private val context: Context,
    private val allSearchesList: List<AllSearches>,
) :
    RecyclerView.Adapter<SavedCategoryAdapter.MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.main_recycler_row_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.categoryTitle.text = allSearchesList[position].searchTitle
        setRecycler(holder.itemRecycler, allSearchesList[position].searchItemList)
    }

    override fun getItemCount(): Int {
        return allSearchesList.size
    }

    private fun setRecycler(
        recyclerView: RecyclerView,
        categoryItemList: List<DBResultImage>
    ) {
        val adapter = SavedImageAdapter(context, categoryItemList)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        recyclerView.adapter = adapter
    }


    class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var categoryTitle: TextView = itemView.findViewById(R.id.row_title)

        var itemRecycler: RecyclerView = itemView.findViewById(R.id.item_recycler)
    }

}