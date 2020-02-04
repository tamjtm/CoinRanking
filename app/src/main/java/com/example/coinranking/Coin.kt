package com.example.coinranking

/**
 * Coin.kt
 *  Object that containg obtained coin data from json
 *
 *      created by Chanisa Phengphon
 *
 *
 *  name : name of coin
 *  description : description of coin
 *  iconUrl : url of coin icon
 *  bDiffView : if true, coin view will be displayed as different view
 */
data class Coin(val name: String,
                val description: String,
                val iconUrl: String,
                val bDiffView: Boolean)