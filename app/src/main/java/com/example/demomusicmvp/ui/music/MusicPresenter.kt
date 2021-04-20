package com.example.demomusicmvp.ui.music

import com.example.demomusicmvp.data.OnDataCallback
import com.example.demomusicmvp.data.model.Song
import com.example.demomusicmvp.data.repository.SongRepository

class MusicPresenter(
    private val view: MusicContact.View,
    private val songRepository: SongRepository
) : MusicContact.Presenter {
    
    override fun getListSong() {
        songRepository.getListSong(object : OnDataCallback<MutableList<Song>> {
            override fun onSuccess(data: MutableList<Song>) {
                view.showListSong(data)
            }

            override fun onFailure(throwable: Throwable) {
                view.showFailure(throwable.message.toString())
            }

        })
    }

    override fun playSong() {
        view.setPauseButton()
    }

    override fun stopSong() {
        view.setPlayButton()
    }

    override fun start() {
        getListSong()
    }
}
