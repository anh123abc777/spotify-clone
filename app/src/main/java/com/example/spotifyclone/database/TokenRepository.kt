package com.example.spotifyclone.database

import androidx.lifecycle.LiveData
import com.example.spotifyclone.database.Token
import com.example.spotifyclone.database.TokenDao


class TokenRepository constructor(private val tokenDao: TokenDao){
    var token: LiveData<Token> = tokenDao.getToken()

    fun insert(token: Token){
        tokenDao.insert(token)
    }

    suspend fun clear(){
        tokenDao.clear()
    }

    suspend fun update(token: Token){
        tokenDao.update(token)
    }
}