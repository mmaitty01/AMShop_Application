package org.swu.aeymai.amshop.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.swu.aeymai.amshop.R

class OnBoardingViewAdapter(private val pageLists: List<OnBoardingViewItem>) : RecyclerView.Adapter<OnBoardingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnBoardingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.onboarding_temp, parent, false)
        return OnBoardingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return pageLists.size
    }

    override fun onBindViewHolder(viewHolder: OnBoardingViewHolder, currentPage: Int) {
        val viewItem = pageLists[currentPage]
        viewHolder.bind(viewItem)
    }
}