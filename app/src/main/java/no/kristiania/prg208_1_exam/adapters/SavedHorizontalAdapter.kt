package no.kristiania.prg208_1_exam.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import no.kristiania.prg208_1_exam.R
import no.kristiania.prg208_1_exam.Utils
import no.kristiania.prg208_1_exam.models.DBResultImage

class SavedHorizontalAdapter(private val results : ArrayList<DBResultImage>, private val clickListener: OnRecyclerItemClick): RecyclerView.Adapter<SavedHorizontalAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.saved_horizontal_recycler, parent, false)
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(results[position])
        holder.itemView.setOnClickListener {
            clickListener.onItemClickListener(results[position])
        }
    }

    override fun getItemCount(): Int {
        return results.size
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val imageView: ImageView = view.findViewById(R.id.item_image)

        fun bind(data: DBResultImage) {
            val bitmap = data.imageBlob?.let { Utils.byteArrayToBitmap(it) }
            imageView.setImageBitmap(bitmap)
        }

    }

    interface OnRecyclerItemClick {
        fun onItemClickListener(data: DBResultImage)
    }
}