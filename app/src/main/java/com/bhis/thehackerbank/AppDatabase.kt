package com.bhis.thehackerbank

import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [Transaction::class, CachedUser::class], version = 2, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao() : TransactionDao
//    abstract fun userDao() : TransactionDao

//    companion object{
//        @Volatile
//        private var INSTANCE
//    }
}