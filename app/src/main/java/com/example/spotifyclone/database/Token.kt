package com.example.spotifyclone.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "token_table")
data class Token (
    @PrimaryKey
    var value: String = ""
    )