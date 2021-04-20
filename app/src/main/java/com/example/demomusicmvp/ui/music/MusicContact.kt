package com.example.demomusicmvp.ui.music

import com.example.demomusicmvp.base.BasePresenter
import com.example.demomusicmvp.data.model.Song

interface MusicContact {
    interface View {
        fun setPlayButton()
        fun setPauseButton()
        fun showListSong(songs: MutableList<Song>)
        fun showFailure(message: String)
    }

    interface Presenter : BasePresenter<View> {
        fun getListSong()
        fun playSong()
        fun stopSong()
    }
}
