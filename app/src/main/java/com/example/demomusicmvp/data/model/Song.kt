package com.example.demomusicmvp.data.model

import android.database.Cursor
import android.provider.MediaStore

data class Song(var id: Long, var title: String, var pathSong: String, var duration: Int) {
    constructor(cursor: Cursor) : this(
        cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID)),
        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)),
        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)),
        cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
    )
}
