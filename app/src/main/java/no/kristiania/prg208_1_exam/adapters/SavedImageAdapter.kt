package no.kristiania.prg208_1_exam.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import no.kristiania.prg208_1_exam.*

import com.squareup.picasso.Callback
import no.kristiania.prg208_1_exam.models.DBResultImage
import java.lang.Exception


class SavedImageAdapter(
    private val context: Context,
    private val categoryItemList: List<DBResultImage>
) :
    RecyclerView.Adapter<SavedImageAdapter.CategoryItemViewHolder>() {

    private lateinit var mListener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {

        return CategoryItemViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.category_row_items, parent, false), mListener
        )
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        val resultImage = categoryItemList[position]

        Log.d("i_debug", "onBindViewHolder: Loading image")

        Picasso.get().load(resultImage.imageLink).placeholder(R.drawable.result_image_placeholder).into(holder.itemImage, object : Callback.EmptyCallback() {
            override fun onSuccess() {
                Log.d("i_debug", "onSuccess: Image loaded!")
            }

            override fun onError(e: Exception?) {
                Log.e("i_debug", "onError: Something went wrong")
            }
        })
    }

    override fun getItemCount(): Int {
        return categoryItemList.size
    }

    class CategoryItemViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

        var itemImage: ImageView = itemView.findViewById(R.id.item_image)

    }

}