package com.github.ojh.firebaseauthsample

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by ohjaehwan on 2017. 2. 15..
 */
class MainAdapter : RecyclerView.Adapter<MainViewHolder>() {

    val chickens by lazy { arrayListOf<Chicken>() }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.view_chicken, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder?, position: Int) {
        holder?.bind(chickens[position])
    }

    override fun getItemCount() = chickens.size

    fun add(items: List<Chicken>) {
        chickens.addAll(items)
        notifyDataSetChanged()
    }

    fun replace(items: List<Chicken>) {
        chickens.clear()
        add(items)
    }
}