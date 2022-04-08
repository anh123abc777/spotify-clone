package com.example.spotifyclone.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Token::class], version = 1, exportSchema = false)
abstract class TonezoneDB : RoomDatabase() {
    abstract val tokenDao: TokenDao

    companion object{

        @Volatile
        private var INSTANCE: TonezoneDB? = null

        fun getInstance(context: Context): TonezoneDB {
            synchronized(this){
                var instance = INSTANCE

                if(instance==null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        TonezoneDB::class.java,
                        "tonezone_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return  instance
            }
        }
    }
}