package github.com.rhacco.dota2androidapp.lists

import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.api.LeaderboardsResponse
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_leaderboards.*

class LeaderboardsAdapter(context: Context) : RecyclerView.Adapter<LeaderboardsViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val mTopRanksToHighlight: List<Int> = listOf(10, 50, 100, 500, 1000)
    private var mItemsData: List<LeaderboardsResponse.Entry> = listOf()
    private var mShownItemsData: List<LeaderboardsResponse.Entry> = listOf()

    fun update(leaderboard: List<LeaderboardsResponse.Entry>) {
        mItemsData =
                if (leaderboard.size > 2000)
                    leaderboard.dropLast(leaderboard.size - 2000)
                else
                    leaderboard
        showAllEntries()
    }

    fun handleSearchQuery(query: String) {
        mShownItemsData = mItemsData.filter { it.name.contains(query, true) }
        notifyDataSetChanged()
    }

    fun showAllEntries() {
        if (mShownItemsData != mItemsData) {
            mShownItemsData = mItemsData
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = mShownItemsData.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LeaderboardsViewHolder =
            LeaderboardsViewHolder(mInflater.inflate(R.layout.item_leaderboards, parent, false))

    override fun onBindViewHolder(holder: LeaderboardsViewHolder, position: Int) {
        val rank = mShownItemsData[position].rank
        holder.rank.text = rank.toString()
        if (mTopRanksToHighlight.contains(rank)) {
            holder.rank.setTextColor(ContextCompat.getColor(
                    App.instance.applicationContext, R.color.leaderboard_top_position))
            holder.rank.setTypeface(null, Typeface.BOLD)
        } else {
            holder.rank.setTextColor(ContextCompat.getColor(
                    App.instance.applicationContext, R.color.text_general))
            holder.rank.setTypeface(null, Typeface.NORMAL)
        }
        holder.name.text = mShownItemsData[position].name
        val iconId = when {
            mShownItemsData[position].rank_change == "up" ->
                App.instance.resources.getIdentifier(
                        "green_triangle_up", "drawable", App.instance.packageName)
            mShownItemsData[position].rank_change == "down" ->
                App.instance.resources.getIdentifier(
                        "red_triangle_down", "drawable", App.instance.packageName)
            mShownItemsData[position].rank_change == "same" ->
                App.instance.resources.getIdentifier(
                        "yellow_circle", "drawable", App.instance.packageName)
            else -> 0
        }
        if (iconId > 0) {
            holder.rank_change.setImageDrawable(
                    ContextCompat.getDrawable(App.instance.applicationContext, iconId))
            holder.rank_change.visibility = View.VISIBLE
        } else
            holder.rank_change.visibility = View.GONE
    }
}

class LeaderboardsViewHolder(override val containerView: View?) :
        RecyclerView.ViewHolder(containerView), LayoutContainer