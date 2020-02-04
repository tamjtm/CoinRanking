package com.example.coinranking

import android.app.Activity
import android.graphics.Typeface
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.coins_standard_item.view.*

/**
 * CoinAdapter
 *  Adapter of coin object using with RecylerView
 *
 *      created by Chanisa Phengphon
 */
class CoinAdapter(val coins: ArrayList<Coin>, val activity: Activity)
    : RecyclerView.Adapter<CoinViewHolder>() {

    /** type of view */
    companion object{
        const val STANDARD_VIEW = 0
        const val DIFFERENT_VIEW = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        /* assign item view according to view type */
        val itemView = when(viewType){
            STANDARD_VIEW -> LayoutInflater.from(parent.context).inflate(
                R.layout.coins_standard_item,
                parent,
                false)
            else -> LayoutInflater.from(parent.context).inflate(
                R.layout.coins_different_item,
                parent,
                false)
        }
        return CoinViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return coins.count()
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        holder.bind(coins[position], activity)
    }

    override fun getItemViewType(position: Int): Int {
        return when (coins[position].bDiffView) {
            true -> DIFFERENT_VIEW
            else -> STANDARD_VIEW
        }
    }
}

/**
 * CoinViewHolder
 *  holder of item view using with RecylerView
 *
 *      created by Chanisa Phengphon
 */
class CoinViewHolder(itemView: View)
    : RecyclerView.ViewHolder(itemView) {

    /** coin name text view */
    val nameItemView = itemView.itemNameTextView

    /** coin description text view */
    val descriptionItemView = itemView.itemDescriptionTextView

    /** coin icon image view */
    val iconItemView = itemView.itemIconImageView

    /**
     * bind data from coin object with item view
     */
    fun bind(coin: Coin, activity: Activity){
        /* assign coin name to nameItemView */
        nameItemView.text = coin.name

        /* item view will not display a description at every 5 coins */
        if(!coin.bDiffView) {
            if (!coin.description.equals("null")) {
                descriptionItemView.text = coin.description
                descriptionItemView.setTypeface(null, Typeface.NORMAL)
            } else {
                descriptionItemView.text = "(no description)"
                descriptionItemView.setTypeface(null, Typeface.ITALIC)
            }
        }

        /* load icon image to iconItemView */
        GlideToVectorYou
            .init()
            .with(activity)
            .setPlaceHolder(R.drawable.ic_launcher_background, R.mipmap.ic_launcher)
            .load(Uri.parse(coin.iconUrl), iconItemView)
    }
}
