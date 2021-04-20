package com.example.demomusicmvp.data.repository

import com.example.demomusicmvp.data.OnDataCallback
import com.example.demomusicmvp.data.model.Song
import com.example.demomusicmvp.data.source.SongDataSource

class SongRepository private constructor(private val songLocalData: SongDataSource) :
    SongDataSource {

    override fun getListSong(callback: OnDataCallback<MutableList<Song>>) {
        songLocalData.getListSong(callback)
    }

    companion object {
        private var instance: SongRepository? = null

        fun getInstance(songLocalData: SongDataSource) =
            instance ?: SongRepository(songLocalData).also { instance = it }
    }
}
