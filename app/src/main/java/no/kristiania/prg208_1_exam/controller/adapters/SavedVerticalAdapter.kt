package no.kristiania.prg208_1_exam.controller.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import no.kristiania.prg208_1_exam.R
import no.kristiania.prg208_1_exam.view.SavedActivity
import no.kristiania.prg208_1_exam.controller.utils.Utils
import no.kristiania.prg208_1_exam.view.fragments.ChosenImageFragment
import no.kristiania.prg208_1_exam.model.models.AllSearches
import no.kristiania.prg208_1_exam.model.models.DBResultImage

class SavedVerticalAdapter(private val activity: SavedActivity, private val fragment: ChosenImageFragment, private val results : ArrayList<AllSearches>): RecyclerView.Adapter<SavedVerticalAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.saved_vertical_recycler, parent, false)
        return ViewHolder(activity, fragment, inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(results[position])
    }

    override fun getItemCount(): Int {
        return results.size
    }

    class ViewHolder(val activity: SavedActivity, val fragment: ChosenImageFragment, val view: View): RecyclerView.ViewHolder(view), SavedHorizontalAdapter.OnRecyclerItemClick {

        private val childRecyclerView: RecyclerView = view.findViewById(R.id.rl_child_recycler_view)

        fun bind(data: AllSearches) {
            childRecyclerView.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(DividerItemDecoration(context, 0))
                adapter = SavedHorizontalAdapter(data.searchItemList, this@ViewHolder)
            }

        }

        override fun onItemClickListener(data: DBResultImage) {
            val chosenBitmapImage = data.imageBlob?.let { Utils.byteArrayToBitmap(it) }
            val origDBImageId = data.originalImgID?.toLong()!!

            val bundle = Bundle()
            bundle.putParcelable("dbResultImage", data)
            bundle.putParcelable("chosenBitmapImage", chosenBitmapImage)
            bundle.putLong("origDBImageId", origDBImageId)

            fragment.arguments = bundle

            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.sa_content_layout, fragment, "content_fragment").commit()
        }

    }
}