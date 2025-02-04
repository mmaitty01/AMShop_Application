package org.swu.aeymai.amshop.Adapter

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import org.swu.aeymai.amshop.R


class PromotionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val imageView = view.findViewById<ImageView>(R.id.iv_promotion)

    fun bind(imageId: Int) {
        imageView.setImageResource(imageId)
    }
}