package com.example.demomusicmvp.service

import android.app.Service
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.media.AudioManager.STREAM_MUSIC
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.os.PowerManager
import android.provider.MediaStore
import android.widget.MediaController
import com.example.demomusicmvp.data.model.Song
import com.example.demomusicmvp.utils.AppUtil.position
import com.example.demomusicmvp.utils.ConstantExt.NOTIFY_ID

class SongService : Service(),
    MediaPlayer.OnCompletionListener,
    MediaPlayer.OnErrorListener,
    MediaController.MediaPlayerControl {

    private val binder = SongBinder()
    private var notificationSong: SongNotification? = null
    private var player = MediaPlayer()
    private var listSong = mutableListOf<Song>()
    var isPause = false
    var isPlay = false
    var isNext = false

    val bindList = fun(list: MutableList<Song>) { listSong = list }

    override fun onCreate() {
        super.onCreate()
        if (notificationSong == null) {
            initPlayer()
            notificationSong = SongNotification()
        }
    }

    override fun onBind(intent: Intent?): IBinder = binder

    override fun onUnbind(intent: Intent?): Boolean {
        player.apply {
            stop()
            release()
        }
        return false
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        mp?.reset()
        return false
    }

    override fun onCompletion(mp: MediaPlayer?) {
        position++
        if (position == listSong.size) position = 0
        playSong(applicationContext)
        isNext = true
    }

    override fun start() {
        isPause = false
        player.start()
    }

    override fun pause() {
        isPause = true
        player.pause()
    }

    override fun getDuration() = player.duration

    override fun getCurrentPosition() = player.currentPosition

    override fun seekTo(pos: Int) = player.seekTo(pos)

    override fun isPlaying() = isPlay

    override fun getBufferPercentage() = 0

    override fun canPause() = true

    override fun canSeekBackward() = true

    override fun canSeekForward() = true

    override fun getAudioSessionId() = player.audioSessionId

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(true)
        player.release()
    }

    private fun initPlayer() {
        player.apply {
            setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
            setAudioStreamType(STREAM_MUSIC)
            setOnCompletionListener(this@SongService)
            setOnErrorListener(this@SongService)
            isLooping = true
        }
    }

    private fun pushNotify() {
        notificationSong?.sendNotification(this, listSong[position].title)
        startForeground(NOTIFY_ID, SongNotification.notification)
    }

    fun playSong(context: Context) {
        player.reset()
        val currentSong = listSong[position].id
        val uri = ContentUris.withAppendedId(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currentSong
        )
        try {
            player.setDataSource(context, uri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        player.apply {
            setOnPreparedListener { mp -> mp.start() }
            prepare()
        }
        pushNotify()
        isPlay = true
    }

    inner class SongBinder : Binder() {
        fun getService(): SongService = this@SongService
    }

    companion object {
        fun getIntent(context: Context): Intent = Intent(context, SongService::class.java)
    }
}
