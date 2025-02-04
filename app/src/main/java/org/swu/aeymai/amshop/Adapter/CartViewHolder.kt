package org.swu.aeymai.amshop.Adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.swu.aeymai.amshop.R

class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val nameprod= view.findViewById<TextView>(R.id.Cart_name)
    private val imageView = view.findViewById<ImageView>(R.id.cart_p)
    private val price = view.findViewById<TextView>(R.id.Cart_price)
    private val context = view.context

    fun bind(cartViewItem: CartViewItem) {
        nameprod.text = cartViewItem.nameprod
        Glide.with(context).load(cartViewItem.img).into(imageView)
        price.text = "฿ " + cartViewItem.priec
    }
}