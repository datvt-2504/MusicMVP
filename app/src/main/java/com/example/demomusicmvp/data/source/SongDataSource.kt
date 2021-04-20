package com.example.demomusicmvp.data.source

import com.example.demomusicmvp.data.OnDataCallback
import com.example.demomusicmvp.data.model.Song

interface SongDataSource {
        fun getListSong(callback: OnDataCallback<MutableList<Song>>)
}
