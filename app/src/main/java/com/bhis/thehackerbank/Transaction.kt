package com.bhis.thehackerbank

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int, // auto generated
    val from_acct: String,
    val to_acct: String,
    val amount: Double,
    val description: String,
    val whoami: String
) {
}

@Entity(tableName = "userAccounts")
data class userAccounts(
    @PrimaryKey(autoGenerate = true)
    val id: Int, // auto generated
    val account_type: String, // not sure what the purpose will be yet.
    val balance: Double) {
}

@Entity(tableName = "CachedUser")
data class CachedUser(
    @PrimaryKey(autoGenerate = true)
    val id: Int, // auto
    val name: String,
    val loggedinsince: Int) {

    }