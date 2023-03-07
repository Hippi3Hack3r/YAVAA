package com.bhis.thehackerbank

import androidx.room.*
// TODO: update thes function names so we can have multiple tables without it being confusing.
@Dao
interface TransactionDao {
    @Query("SELECT * from transactions")
    fun getAll(): List<Transaction>

    //@Insert(onConflict = OnConflictStrategy.IGNORE) <- how vulnerable do we want it?
    @Insert
    fun insertAll(vararg transaction: Transaction)

    @Delete
    fun delete(transaction: Transaction)

    @Query("DELETE FROM transactions")
    fun deleteAll()

    @Update
    fun update(vararg transaction: Transaction)
}