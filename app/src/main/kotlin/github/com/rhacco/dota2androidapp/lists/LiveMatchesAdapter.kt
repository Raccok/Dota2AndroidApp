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

    // Add tournament matches first, then sort by average MMR (descending). Tournament matches have
    // an average MMR of 0, ranked matches have an average MMR > 0.
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
        holder.radiant_player0?.text = itemData.mRadiantPlayer0
        holder.radiant_player1?.text = itemData.mRadiantPlayer1
        holder.radiant_player2?.text = itemData.mRadiantPlayer2
        holder.radiant_player3?.text = itemData.mRadiantPlayer3
        holder.radiant_player4?.text = itemData.mRadiantPlayer4
        holder.dire_player0?.text = itemData.mDirePlayer0
        holder.dire_player1?.text = itemData.mDirePlayer1
        holder.dire_player2?.text = itemData.mDirePlayer2
        holder.dire_player3?.text = itemData.mDirePlayer3
        holder.dire_player4?.text = itemData.mDirePlayer4
    }
}

class LiveMatchesItemData {
    var mAverageMMR = 0  // Used to sort list by average MMR
    var mTitle: String = ""
    var mRadiantPlayer0: String = ""
    var mRadiantPlayer1: String = ""
    var mRadiantPlayer2: String = ""
    var mRadiantPlayer3: String = ""
    var mRadiantPlayer4: String = ""
    var mDirePlayer0: String = ""
    var mDirePlayer1: String = ""
    var mDirePlayer2: String = ""
    var mDirePlayer3: String = ""
    var mDirePlayer4: String = ""
}

class LiveMatchesViewHolder(view: View?, override val containerView: View? = view) :
        RecyclerView.ViewHolder(view), LayoutContainer
