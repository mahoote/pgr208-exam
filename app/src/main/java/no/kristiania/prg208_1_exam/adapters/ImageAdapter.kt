package no.kristiania.prg208_1_exam.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import no.kristiania.prg208_1_exam.R
import no.kristiania.prg208_1_exam.models.ResultImage

class ImageAdapter(private val results : ArrayList<ResultImage>): RecyclerView.Adapter<ImageAdapter.ResultsViewHolder>() {

    private lateinit var mListener: OnItemClickListener

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

        holder.img.setImageResource(currentItem.img)
    }

    override fun getItemCount(): Int {
        return results.size
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

