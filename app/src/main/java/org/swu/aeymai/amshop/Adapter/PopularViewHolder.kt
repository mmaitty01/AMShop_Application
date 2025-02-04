package org.swu.aeymai.amshop.Adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.swu.aeymai.amshop.R

class PopularViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    private val imageView = view.findViewById<ImageView>(R.id.iv_popular)
    private val textView = view.findViewById<TextView>(R.id.tv_popular)
    private val textView1 = view.findViewById<TextView>(R.id.tv_popular1)


    fun bind(popularViewItem: PopularViewItem) {
        imageView.setImageResource(popularViewItem.imageId)
        textView.text = popularViewItem.title
        textView1.text = popularViewItem.b.toString()
    }
}