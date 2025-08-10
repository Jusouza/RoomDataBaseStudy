package com.devspace.taskbeats

import androidx.room.Database
import androidx.room.RoomDatabase

//TODO: pesquisar pra que serve - [CategoryEntity::class], version = 1 (muda a versao apenas quando eu troco a key do meu entity? plano de migracao nao foi feito?)
@Database([CategoryEntity::class, TaskEntity::class], version = 3)
abstract class TaskBeatDataBase : RoomDatabase(){
        abstract fun getCategoryDao(): CategoryDao
        abstract fun getTaskDao(): TaskDao

}