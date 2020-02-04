package com.example.coinranking

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.IOException
import java.net.URL

/**
 * MainActivity.kt
 *  An Android Application that displaying data from coinranking API
 *
 *      created by Chanisa Phengphon
 */
class MainActivity : AppCompatActivity() {

    /** collection containing coin objects */
    private val coins = ArrayList<Coin>()

    /** coin adapter object for RecyclerView */
    private lateinit var adapter: CoinAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        /* call superclass onCreate method  */
        super.onCreate(savedInstanceState)
        try {
            /* hide action bar in application */
            this.supportActionBar!!.hide()
        } catch (e: NullPointerException) {
        }
        setContentView(R.layout.activity_main)

        /* add divider decoration to RecyclerView */
        mainRecyclerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )

        /* assign LinearLayoutManager to manage RecyclerView's layout manager */
        mainRecyclerView.layoutManager = LinearLayoutManager(this)

        /* create coin adapter then assign to MainActivity's adapter and RecyclerView's adapter */
        adapter = CoinAdapter(coins, this)
        mainRecyclerView.adapter = adapter

        /* load coinranking data and collect in coins collection */
        CoinRankingLoaderTask().execute()

        /* set action to refresh when pull the screen */
        mainSwipeRefreshLayout.setOnRefreshListener {
            coins.clear()
            adapter = CoinAdapter(coins, this)
            mainRecyclerView.adapter = adapter
            CoinRankingLoaderTask().execute()
            mainSwipeRefreshLayout.isRefreshing = false
        }
    }

    /**
     * collect coin data from json to coins collection
     * @param json data in JSON format
     */
    fun updateCoins(json: String) {
        /* get JSON object from data */
        val jsonObj = JSONObject(json)

        /* get values object of data key from JSON object and collect in dataObj */
        val dataObject = jsonObj.getJSONObject("data")

        /* get values array of coins key from dataObj and collect in coinsArray*/
        val coinsArray = dataObject.getJSONArray("coins")

        /* clear old values from coins */
        coins.clear()

        /* iterator all object in coinsArray */
        for (coinId in 0 until coinsArray.length()) {
            /* get coin data from coinsArray */
            val coinObj = coinsArray.getJSONObject(coinId)

            /* every 5 coins view display with different view */
            val diffView = when ((coinId+1)%5){
                0 -> true
                else -> false
            }

            /* create coin instance with above data */
            val coin = Coin(
                coinObj.getString("name"),
                coinObj.getString("description"),
                coinObj.getString("iconUrl"),
                diffView
            )

            /* collect coin instance in coins collection */
            coins.add(coin)
        }

        /* notify to adapter */
        adapter.notifyDataSetChanged()
    }


    /**
     * CoinRankingLoaderTask
     *  Task that loading json data from url in background
     */
    inner class CoinRankingLoaderTask: AsyncTask<String, String, String>() {
        override fun doInBackground(vararg params: String?): String {
            val url = URL("https://api.coinranking.com/v1/public/coins")
            return try {
                val json = url.readText()
                json
            } catch (e: IOException) {
                ""
            }
        }

        override fun onPostExecute(result: String?){
            if(result != null){
                if(result != ""){
                    updateCoins(result)
                }
            }
        }
    }
}
