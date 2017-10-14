package github.com.rhacco.dota2androidapp.lists

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.com.rhacco.dota2androidapp.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_live_matches.*

class LiveMatchesAdapter(context: Context) : RecyclerView.Adapter<LiveMatchesViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val mItemsData: MutableList<LiveMatchesItemData> = mutableListOf()

    // Add tournament matches first, then sort by average MMR (descending)
    fun add(itemData: LiveMatchesItemData) {
        if (itemData.mAverageMMR < 1) {
            mItemsData.add(0, itemData)
        } else {
            val index = mItemsData.count { it.mAverageMMR < 1 || it.mAverageMMR >= itemData.mAverageMMR }
            mItemsData.add(index, itemData)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mItemsData.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LiveMatchesViewHolder =
            LiveMatchesViewHolder(mInflater.inflate(R.layout.item_live_matches, parent, false))

    override fun onBindViewHolder(holder: LiveMatchesViewHolder, position: Int) {
        val itemData = mItemsData[position]
        holder.title?.text = itemData.mTitle
        holder.player_blue?.text = itemData.mBlue
        holder.player_teal?.text = itemData.mTeal
        holder.player_purple?.text = itemData.mPurple
        holder.player_yellow?.text = itemData.mYellow
        holder.player_orange?.text = itemData.mOrange
        holder.player_pink?.text = itemData.mPink
        holder.player_gray?.text = itemData.mGray
        holder.player_light_blue?.text = itemData.mLightBlue
        holder.player_dark_green?.text = itemData.mDarkGreen
        holder.player_brown?.text = itemData.mBrown
    }
}

class LiveMatchesItemData {
    var mAverageMMR = 0  // Used to sort list by average MMR
    var mTitle: String = ""
    var mBlue: String = ""
    var mTeal: String = ""
    var mPurple: String = ""
    var mYellow: String = ""
    var mOrange: String = ""
    var mPink: String = ""
    var mGray: String = ""
    var mLightBlue: String = ""
    var mDarkGreen: String = ""
    var mBrown: String = ""
}

class LiveMatchesViewHolder(view: View?, override val containerView: View? = view) :
        RecyclerView.ViewHolder(view), LayoutContainer
