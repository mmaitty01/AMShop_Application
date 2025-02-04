package org.swu.aeymai.amshop.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.swu.aeymai.amshop.R

class CartAdapter (private val pageLists: List<CartViewItem>) : RecyclerView.Adapter<CartViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_cart_temp, parent, false)
        return CartViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pageLists.size
    }

    override fun onBindViewHolder(viewHolder: CartViewHolder, currentPage: Int) {
        val viewItem = pageLists[currentPage]
        viewHolder.bind(viewItem)
    }
}