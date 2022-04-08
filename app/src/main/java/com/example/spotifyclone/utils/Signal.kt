package com.example.spotifyclone.utils

import com.example.tonezone.R

enum class Signal {
    @Suppress("EnumEntryName")
    LIKE_PLAYLIST,
    LIKED_PLAYLIST,
    HIDE_THIS_SONG,
    ADD_TO_PLAYLIST,
    VIEW_ARTIST,
    REMOVE_FROM_THIS_PLAYLIST,
    ADD_TO_QUEUE,

    VIEW_ALBUM,

    ADD_SONGS,
    EDIT_PLAYLIST,
    DELETE_PLAYLIST,

    LIKE_TRACK,
    LIKED_TRACK,

    PIN_ARTIST,
    UNPIN_ARTIST,
    PIN_PLAYLIST,
    UNPIN_PLAYLIST,
    STOP_FOLLOWING,
    SHARE
}

fun convertSignalToText(signal: Signal): String =
    when(signal){
        Signal.LIKE_PLAYLIST -> "Like"
        Signal.LIKED_PLAYLIST -> "Liked"
        Signal.HIDE_THIS_SONG -> "Hide this song"
        Signal.ADD_TO_PLAYLIST -> "Add to playlist"
        Signal.VIEW_ARTIST -> "View artist"
        Signal.REMOVE_FROM_THIS_PLAYLIST -> "Remove from this playlist"
        Signal.ADD_TO_QUEUE -> "Add to queue"
        Signal.VIEW_ALBUM -> "View album"
        Signal.ADD_SONGS -> "Add songs"
        Signal.EDIT_PLAYLIST -> "Edit playlist"
        Signal.DELETE_PLAYLIST -> "Delete playlist"
        Signal.LIKE_TRACK -> "Like"
        Signal.LIKED_TRACK -> "Liked"
        Signal.PIN_ARTIST -> "Pin artist"
        Signal.UNPIN_ARTIST -> "Unpin artist"
        Signal.PIN_PLAYLIST -> "Pin playlist"
        Signal.UNPIN_PLAYLIST -> "Unpin playlist"
        Signal.SHARE -> "Share"
        Signal.STOP_FOLLOWING -> "Stop following"

    }

fun convertSignalToIcon(signal: Signal): Int =
    when(signal){
        Signal.LIKE_PLAYLIST -> R.drawable.ic_unlike
        Signal.LIKED_PLAYLIST -> R.drawable.ic_favorite
        Signal.HIDE_THIS_SONG -> R.drawable.ic_hide
        Signal.ADD_TO_PLAYLIST -> R.drawable.ic_add_playlist
        Signal.VIEW_ARTIST -> R.drawable.ic_view_artist
        Signal.REMOVE_FROM_THIS_PLAYLIST -> R.drawable.ic_remove
        Signal.ADD_TO_QUEUE -> R.drawable.ic_add_queue
        Signal.VIEW_ALBUM -> R.drawable.ic_view_album
        Signal.ADD_SONGS -> R.drawable.ic_grid
        Signal.EDIT_PLAYLIST -> R.drawable.ic_grid
        Signal.DELETE_PLAYLIST -> R.drawable.ic_close
        Signal.LIKE_TRACK -> R.drawable.ic_unlike
        Signal.LIKED_TRACK -> R.drawable.ic_favorite
        Signal.PIN_ARTIST -> R.drawable.ic_pin
        Signal.UNPIN_ARTIST -> R.drawable.ic_pin
        Signal.PIN_PLAYLIST -> R.drawable.ic_pin
        Signal.UNPIN_PLAYLIST -> R.drawable.ic_pin
        Signal.SHARE -> R.drawable.ic_view_artist
        Signal.STOP_FOLLOWING -> R.drawable.ic_close
    }
