package org.swu.aeymai.amshop.Adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.swu.aeymai.amshop.R

class PointViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val textTitle = view.findViewById<TextView>(R.id.tvShopTitle)
    private val textDescr = view.findViewById<TextView>(R.id.tvShopDescr)
    private val imageView = view.findViewById<ImageView>(R.id.ivShopImg)

    fun bind(item: PointViewItem) {
        textTitle.text = item.title
        textDescr.text = item.description
        imageView.setImageResource(item.imageId)
    }
}