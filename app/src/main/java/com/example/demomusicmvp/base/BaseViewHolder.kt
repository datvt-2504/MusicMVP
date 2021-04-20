package com.example.demomusicmvp.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<T>(itemView: View, onItemClick: (T) -> Unit) :
    RecyclerView.ViewHolder(itemView) {

    private var itemData: T? = null

    init {
        itemView.setOnClickListener { itemData?.let { onItemClick(it) } }
    }

    open fun onBindData(itemData: T) {
        this.itemData = itemData
    }
}
