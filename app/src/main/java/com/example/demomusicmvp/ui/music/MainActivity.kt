package com.example.demomusicmvp.ui.music

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.os.IBinder
import android.view.View
import com.example.demomusicmvp.R
import com.example.demomusicmvp.base.BaseActivity
import com.example.demomusicmvp.data.model.Song
import com.example.demomusicmvp.data.repository.SongRepository
import com.example.demomusicmvp.data.source.local.SongLocalSource
import com.example.demomusicmvp.service.SongService
import com.example.demomusicmvp.ui.adapter.MusicAdapter
import com.example.demomusicmvp.utils.AppUtil.position
import com.example.demomusicmvp.utils.gone
import com.example.demomusicmvp.utils.show
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(R.layout.activity_main), MusicContact.View, View.OnClickListener {

    private var isBind = false
    private var listSongLocal = mutableListOf<Song>()
    private var musicPresenter: MusicContact.Presenter? = null
    private var songService: SongService? = null
    private var serviceIntent: Intent? = null
    private val musicAdapter = MusicAdapter(this::onClickItem)

    private val connectionService = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as SongService.SongBinder
            songService = binder.getService()
            songService?.let { it.bindList(listSongLocal) }
            isBind = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBind = false
        }
    }

    override fun onCreatedView() {
        checkPermission()
        groupBottomPlay.gone()
        initOnClick()
        musicPresenter = MusicPresenter(
            this,
            SongRepository.getInstance(SongLocalSource.getInstanceLocalSource(this))
        )
        musicPresenter?.getListSong()
    }

    override fun onStart() {
        super.onStart()
        serviceIntent = SongService.getIntent(this)
        bindService(serviceIntent, connectionService, BIND_AUTO_CREATE)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonPlaySong -> songService?.isPlaying?.let { setPlayingState(it) }
            R.id.buttonNextSong -> {
                position++
                resetPlaying()
            }
            R.id.buttonPreviousSong -> {
                position--
                resetPlaying()
            }
        }
    }

    override fun setPlayButton() {
        buttonPlaySong.setImageResource(R.drawable.ic_play)
    }

    override fun setPauseButton() {
        buttonPlaySong.setImageResource(R.drawable.ic_pause)
    }

    override fun showListSong(songs: MutableList<Song>) {
        listSongLocal = songs.toMutableList()
        listSongLocal.sortWith(compareBy({ it.title }, { it.title }))
        musicAdapter.updateData(listSongLocal)
        recyclerSongs.adapter = musicAdapter
    }

    override fun showFailure(message: String) {
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(connectionService)
        isBind = false
    }

    private fun initOnClick() {
        buttonPlaySong.setOnClickListener(this)
        buttonNextSong.setOnClickListener(this)
        buttonPreviousSong.setOnClickListener(this)
    }

    private fun resetPlaying() {
        songService?.playSong(applicationContext)
        musicPresenter?.playSong()
        textTitleSongCurrent.text = listSongLocal[position].title
    }

    private fun onClickItem(item: Song) {
        groupBottomPlay.show()
        songService?.playSong(applicationContext)
        musicPresenter?.playSong()
        textTitleSongCurrent.text = item.title
    }

    private fun checkPermission() {
        if ((checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }
    }

    private fun setPlayingState(state: Boolean) {
        if (!state) {
            if (songService?.isPause == true) songService?.start()
            songService?.isPlay = true
            musicPresenter?.playSong()
        } else {
            songService?.pause()
            songService?.isPlay = false
            musicPresenter?.stopSong()
        }
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
    }
}
