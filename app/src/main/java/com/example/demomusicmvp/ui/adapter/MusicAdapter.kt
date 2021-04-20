package com.example.demomusicmvp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.demomusicmvp.R
import com.example.demomusicmvp.base.BaseAdapter
import com.example.demomusicmvp.base.BaseViewHolder
import com.example.demomusicmvp.data.model.Song
import kotlinx.android.synthetic.main.item_song.view.*

class MusicAdapter(private val onItemClick: (Song) -> Unit) :
    BaseAdapter<Song, MusicAdapter.MusicViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder =
        MusicViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_song, parent, false),
            onItemClick
        )

    class MusicViewHolder(
        itemView: View,
        onItemClick: (Song) -> Unit
    ) : BaseViewHolder<Song>(itemView, onItemClick) {

        override fun onBindData(itemData: Song) {
            super.onBindData(itemData)
            with(itemView) {
                itemData.apply {
                    textTitleSong.text = title
                    textPathSong.text = pathSong
                }
            }
        }
    }
}
