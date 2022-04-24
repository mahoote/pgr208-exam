package no.kristiania.prg208_1_exam.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toFile
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import no.kristiania.prg208_1_exam.*
import no.kristiania.prg208_1_exam.models.SearchItem
import android.content.pm.ResolveInfo

import android.content.pm.PackageManager
import android.icu.number.NumberFormatter.with
import com.facebook.shimmer.ShimmerFrameLayout
import com.squareup.picasso.Callback
import com.squareup.picasso.Callback.EmptyCallback
import java.lang.Exception


class CategoryItemRecyclerAdapter(
    private val context: Context,
    private val categoryItemList: List<SearchItem>,
    private val shimmerFrameLayout: ShimmerFrameLayout,
    private val mainCategoryRecycler: RecyclerView?
) :
    RecyclerView.Adapter<CategoryItemRecyclerAdapter.CategoryItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {

        return CategoryItemViewHolder(
            LayoutInflater.from(
                context
            ).inflate(R.layout.category_row_items, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        val searchItem = categoryItemList[position]
//        Picasso.get().load(searchItem.imageUri).into(holder.itemImage)

        Log.d("i_debug", "onBindViewHolder: Loading image")

        Picasso.get().load(searchItem.imageUri).into(holder.itemImage, object : Callback.EmptyCallback() {
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

    class CategoryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.item_image)

    }

}