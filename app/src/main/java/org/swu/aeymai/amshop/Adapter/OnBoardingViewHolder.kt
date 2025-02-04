package org.swu.aeymai.amshop.Adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import org.swu.aeymai.amshop.R

class OnBoardingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val textTitle = view.findViewById<TextView>(R.id.tvOnboardingTitle)

    private val imageView = view.findViewById<ImageView>(R.id.ivOnboarding)

    fun bind(onBoardingViewItem: OnBoardingViewItem) {
        textTitle.text = onBoardingViewItem.title

        imageView.setImageResource(onBoardingViewItem.imageId)
    }
}