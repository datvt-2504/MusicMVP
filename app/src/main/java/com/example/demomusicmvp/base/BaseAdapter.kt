package com.example.demomusicmvp.base

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T, V : BaseViewHolder<T>> : RecyclerView.Adapter<V>() {

    private val items = mutableListOf<T>()

    override fun onBindViewHolder(holder: V, position: Int) {
        getItem(position)?.let { holder.onBindData(it) }
    }

    override fun getItemCount() = items.size

    private fun getItem(position: Int): T? =
        if (position in 0 until itemCount) items[position] else null

    open fun updateData(newData: MutableList<T>) {
        items.clear()
        items.addAll(newData)
        notifyDataSetChanged()
    }
}
