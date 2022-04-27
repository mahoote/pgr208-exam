package no.kristiania.prg208_1_exam.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import no.kristiania.prg208_1_exam.R
import no.kristiania.prg208_1_exam.models.DBResultImage
import no.kristiania.prg208_1_exam.models.ResultImage

class SearchImageAdapter(private val results : ArrayList<DBResultImage>): RecyclerView.Adapter<SearchImageAdapter.ResultsViewHolder>() {

    private lateinit var mListener: OnItemClickListener
    private var viewHolder: ArrayList<ResultsViewHolder> = arrayListOf()

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.result_item, parent, false)
        return ResultsViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        val currentItem = results[position]

        Picasso.get().load(currentItem.imageLink).into(holder.img)
        viewHolder.add(holder)
    }

    override fun getItemCount(): Int {
        return results.size
    }

    fun getHolders(): ArrayList<ResultsViewHolder> {
        return viewHolder
    }

    class ResultsViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView){

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

        val img: ImageView = itemView.findViewById(R.id.ri_img)
    }
}

