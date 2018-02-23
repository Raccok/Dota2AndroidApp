package github.com.rhacco.dotascoop.lists

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.com.rhacco.dotascoop.App
import github.com.rhacco.dotascoop.R
import github.com.rhacco.dotascoop.api.LeaderboardsResponse
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
        mShownItemsData = mItemsData.filter {
            it.name.contains(query, true) || it.rank.toString() == query
        }
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LeaderboardsViewHolder, position: Int) {
        val itemData = mShownItemsData[position]
        val rank = itemData.rank
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
        holder.name.text = itemData.name
        val iconId = when {
            (itemData.last_rank != null && itemData.rank < itemData.last_rank) ||
                    (itemData.new_in_top_100 != null && itemData.new_in_top_100) ->
                App.instance.resources.getIdentifier(
                        "green_triangle_up", "drawable", App.instance.packageName)
            itemData.last_rank == null -> 0
            itemData.rank > itemData.last_rank ->
                App.instance.resources.getIdentifier(
                        "red_triangle_down", "drawable", App.instance.packageName)
            else -> 0
        }
        if (iconId > 0) {
            holder.rank_change_icon.setImageDrawable(
                    ContextCompat.getDrawable(App.instance.applicationContext, iconId))
            holder.rank_change_icon.visibility = View.VISIBLE
            if (itemData.last_rank != null) {
                val rankChange = itemData.last_rank - itemData.rank
                if (rankChange > 0)
                    holder.rank_change.text =
                            App.instance.getString(R.string.sign_plus) + rankChange
                else
                    holder.rank_change.text =
                            App.instance.getString(R.string.sign_minus) + (-rankChange)
            } else
                holder.rank_change.text = "new"
            holder.rank_change.visibility = View.VISIBLE
        } else {
            holder.rank_change_icon.visibility = View.GONE
            holder.rank_change.visibility = View.GONE
        }
    }
}

class LeaderboardsViewHolder(override val containerView: View?) :
        RecyclerView.ViewHolder(containerView), LayoutContainer