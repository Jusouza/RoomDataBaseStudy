package com.devspace.taskbeats

import androidx.room.Database
import androidx.room.RoomDatabase

//TODO: pesquisar pra que serve - [CategoryEntity::class], version = 1
@Database([CategoryEntity::class], version = 1)
abstract class TaskBeatDataBase : RoomDatabase(){
        abstract fun getCategoryDao(): CategoryDao

}