package github.com.rhacco.dota2androidapp.lists

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.com.rhacco.dota2androidapp.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_leaderboards.*

class LeaderboardsAdapter(mContext: Context) : RecyclerView.Adapter<LeaderboardsViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mItemsData: List<String> = listOf()

    fun update(leaderboard: List<String>) {
        mItemsData = leaderboard
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mItemsData.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LeaderboardsViewHolder =
            LeaderboardsViewHolder(mInflater.inflate(R.layout.item_leaderboards, parent, false))

    override fun onBindViewHolder(holder: LeaderboardsViewHolder, position: Int) {
        holder.rank.text = (position + 1).toString()
        holder.player.text = mItemsData[position]
    }
}

class LeaderboardsViewHolder(override val containerView: View?) :
        RecyclerView.ViewHolder(containerView), LayoutContainer