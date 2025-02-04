package org.swu.aeymai.amshop.Adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.swu.aeymai.amshop.R
import org.swu.aeymai.amshop.Util.GlobalBox


class SearchViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    private val imageView = view.findViewById<ImageView>(R.id.imgprod)
    private val namemark = view.findViewById<TextView>(R.id.namemark)
    private val nameprod = view.findViewById<TextView>(R.id.nameprod)
    private val cat = view.findViewById<TextView>(R.id.category)
    private val price = view.findViewById<TextView>(R.id.price)
    private val context = view.context

    fun bind(SearchViewItem: SearchOnlineItem) {
        Glide.with(context).load(SearchViewItem.img).into(imageView)
        namemark.text = SearchViewItem.namemark
        nameprod.text = SearchViewItem.nameprod
        cat.text = SearchViewItem.category
        price.text = "฿ " + SearchViewItem.priec

        price.setOnClickListener {
            GlobalBox.namemark.add(SearchViewItem.namemark)
            GlobalBox.nameprod.add(SearchViewItem.nameprod)
            GlobalBox.img.add(SearchViewItem.img)
            GlobalBox.priec.add(SearchViewItem.priec.toDouble())
            Toast.makeText(context,"add to cart.", Toast.LENGTH_SHORT).show()
        }



    }
}