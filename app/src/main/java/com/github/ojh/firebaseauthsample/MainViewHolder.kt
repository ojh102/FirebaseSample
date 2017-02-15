package com.github.ojh.firebaseauthsample

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.view_chicken.view.*

/**
 * Created by ohjaehwan on 2017. 2. 15..
 */


class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(chicken: Chicken) {
        itemView.tvChicken.text = chicken.name
        Glide.with(itemView.context)
                .load(chicken.url)
                .into(itemView.ivChicken)
    }
}