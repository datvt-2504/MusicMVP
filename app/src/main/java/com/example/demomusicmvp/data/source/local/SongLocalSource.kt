package com.example.demomusicmvp.data.source.local

import android.content.Context
import android.os.Build
import android.provider.MediaStore
import com.example.demomusicmvp.data.LoadDataAsync
import com.example.demomusicmvp.data.OnDataCallback
import com.example.demomusicmvp.data.model.Song
import com.example.demomusicmvp.data.source.SongDataSource
import java.io.File

class SongLocalSource private constructor(private val context: Context) : SongDataSource {

    override fun getListSong(callback: OnDataCallback<MutableList<Song>>) {
        LoadDataAsync<Unit, MutableList<Song>>(callback) {
            getSong()
        }.execute(Unit)
    }

    private fun getSong(): MutableList<Song> {
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val listSong = mutableListOf<Song>()
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION
        )
        val uri =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.getContentUri(
                    MediaStore.VOLUME_EXTERNAL
                )
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }
        val orderBy = MediaStore.Audio.Media.DATE_ADDED
        val cursor =
            context.contentResolver.query(uri, projection, selection, null, " $orderBy DESC ")
        cursor?.let {
            while (it.moveToNext()) {
                listSong.add(Song(it))
            }
            cursor.close()
        }
        return listSong
    }

    companion object {
        private var instance: SongLocalSource? = null

        fun getInstanceLocalSource(context: Context) =
            instance ?: synchronized(this) {
                instance ?: SongLocalSource(context).also {
                    instance = it
                }
            }
    }
}
