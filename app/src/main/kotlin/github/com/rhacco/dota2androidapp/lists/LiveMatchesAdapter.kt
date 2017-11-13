package github.com.rhacco.dota2androidapp.lists

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.entities.ProPlayerEntity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_live_matches.*

class LiveMatchesAdapter(context: Context) : RecyclerView.Adapter<LiveMatchesViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val mItemsData: MutableList<LiveMatchesItemData> = mutableListOf()

    // Add tournament matches first, then sort by average MMR (descending). Tournament matches have
    // an average MMR of 0, ranked matches have an average MMR > 0.
    fun add(newItemData: LiveMatchesItemData): Boolean {
        // Don't add if match already exists
        mItemsData.filter { newItemData.mMatchID == it.mMatchID }.forEach { return false }

        var index = 0
        if (!newItemData.mIsTournamentMatch)
            index = mItemsData.count { it.mIsTournamentMatch || it.mAverageMMR >= newItemData.mAverageMMR }
        mItemsData.add(index, newItemData)
        notifyItemInserted(index)
        return true
    }

    fun setOfficialNames(proPlayers: List<ProPlayerEntity>) {
        proPlayers.forEach {
            var index = 0
            while (index < mItemsData.size) {
                for (player in mItemsData[index].mPlayers)
                    if (player.steamId == it.account_id) {
                        if (it.name != null && player.officialName != it.name) {
                            player.officialName = it.name
                            notifyItemChanged(index)
                        }
                        index = mItemsData.size  // breaks outer while loop
                        break
                    }
                ++index
            }
        }
    }

    fun updateRealtimeStats(newItemData: LiveMatchesItemData) {
        var index = 0
        while (index < mItemsData.size) {
            if (mItemsData[index].mMatchID == newItemData.mMatchID) {
                val oldItemData = mItemsData[index]
                if (oldItemData.mRadiantScore != newItemData.mRadiantScore ||
                        oldItemData.mElapsedTime != newItemData.mElapsedTime ||
                        oldItemData.mDireScore != newItemData.mDireScore ||
                        oldItemData.mGoldAdvantage != newItemData.mGoldAdvantage) {
                    newItemData.mShowAdditionalInfo = oldItemData.mShowAdditionalInfo
                    mItemsData[index] = newItemData
                    notifyItemChanged(index)
                }
                return
            }
            ++index
        }
    }

    fun switchShowAdditionalInfo(itemPosition: Int) {
        mItemsData[itemPosition].mShowAdditionalInfo = !mItemsData[itemPosition].mShowAdditionalInfo
        notifyItemChanged(itemPosition)
    }

    fun remove(matchId: Long) {
        var index = 0
        while (index < mItemsData.size) {
            if (mItemsData[index].mMatchID == matchId) {
                mItemsData.removeAt(index)
                notifyItemRemoved(index)
                return
            }
            ++index
        }
    }

    override fun getItemCount(): Int = mItemsData.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LiveMatchesViewHolder =
            LiveMatchesViewHolder(mInflater.inflate(R.layout.item_live_matches, parent, false), this)

    override fun onBindViewHolder(holder: LiveMatchesViewHolder, position: Int) {
        val itemData = mItemsData[position]
        holder.title?.text = itemData.mTitle
        holder.team_radiant?.text = itemData.mTeamRadiant
        holder.team_dire?.text = itemData.mTeamDire
        bindStats(holder, itemData)
        if (itemData.mPlayers.size == 10) {
            bindPlayerName(holder.radiant_player0, itemData.mPlayers[0], itemData)
            bindPlayerName(holder.radiant_player1, itemData.mPlayers[1], itemData)
            bindPlayerName(holder.radiant_player2, itemData.mPlayers[2], itemData)
            bindPlayerName(holder.radiant_player3, itemData.mPlayers[3], itemData)
            bindPlayerName(holder.radiant_player4, itemData.mPlayers[4], itemData)
            bindPlayerName(holder.dire_player0, itemData.mPlayers[5], itemData)
            bindPlayerName(holder.dire_player1, itemData.mPlayers[6], itemData)
            bindPlayerName(holder.dire_player2, itemData.mPlayers[7], itemData)
            bindPlayerName(holder.dire_player3, itemData.mPlayers[8], itemData)
            bindPlayerName(holder.dire_player4, itemData.mPlayers[9], itemData)
        } else
            Log.d(App.instance.getString(R.string.log_msg_debug),
                    "A Live Matches item has corrupted players data")
        bindIDs(holder, itemData)
    }

    private fun bindStats(holder: LiveMatchesViewHolder, itemData: LiveMatchesItemData) {
        holder.score_radiant?.text = itemData.mRadiantScore.toString()
        val elapsedTimeMin = maxOf(0, Math.floor(itemData.mElapsedTime / 60.0).toInt())
        val elapsedTimeSec = maxOf(0, itemData.mElapsedTime % 60)
        if (elapsedTimeSec < 10)
            holder.elapsed_time?.text = App.instance.getString(R.string.elapsed_time_corrected_sec,
                    elapsedTimeMin, elapsedTimeSec)
        else
            holder.elapsed_time?.text = App.instance.getString(R.string.elapsed_time,
                    elapsedTimeMin, elapsedTimeSec)
        holder.score_dire?.text = itemData.mDireScore.toString()
        val goldAdvantageThousands = Math.floor(Math.abs(itemData.mGoldAdvantage) / 1000.0).toInt()
        val goldAdvantageHundreds = Math.round(Math.abs(itemData.mGoldAdvantage) / 100.0) % 10
        holder.gold_advantage?.text = App.instance.getString(R.string.gold_advantage,
                goldAdvantageThousands, goldAdvantageHundreds)
        val params = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.CENTER_VERTICAL)
        params.leftMargin = 20
        params.rightMargin = 20
        if (itemData.mGoldAdvantage >= 0)
            params.addRule(RelativeLayout.LEFT_OF, R.id.score_radiant)
        else
            params.addRule(RelativeLayout.RIGHT_OF, R.id.score_dire)
        holder.gold_advantage?.layoutParams = params
    }

    private fun bindPlayerName(textView: TextView?, player: Player, itemData: LiveMatchesItemData) =
            when {
                player.officialName.isEmpty() || itemData.mIsTournamentMatch || itemData.mShowAdditionalInfo
                -> {
                    textView?.text = player.currentSteamName
                    textView?.setTextColor(ContextCompat.getColor(
                            App.instance.applicationContext, R.color.text_general))
                }
                else -> {
                    textView?.text = player.officialName
                    textView?.setTextColor(ContextCompat.getColor(
                            App.instance.applicationContext, R.color.text_pro_player))
                }
            }

    private fun bindIDs(holder: LiveMatchesViewHolder, itemData: LiveMatchesItemData) =
            when {
                itemData.mShowAdditionalInfo -> {
                    holder.server_id?.text = App.instance.getString(
                            R.string.additional_info_server_id, itemData.mServerID)
                    holder.server_id?.visibility = View.VISIBLE
                    holder.match_id?.text = App.instance.getString(
                            R.string.additional_info_match_id, itemData.mMatchID)
                    holder.match_id?.visibility = View.VISIBLE
                }
                else -> {
                    holder.server_id?.visibility = View.GONE
                    holder.match_id?.visibility = View.GONE
                }
            }
}

class LiveMatchesItemData {
    var mIsTournamentMatch = false
    var mTitle: String = App.instance.getString(R.string.heading_live_tournament_match)
    var mAverageMMR = 0
    var mRadiantScore = 0
    var mElapsedTime = 0
    var mDireScore = 0
    var mGoldAdvantage = 0
    var mTeamRadiant: String = App.instance.getString(R.string.team_radiant)
    var mTeamDire: String = App.instance.getString(R.string.team_dire)
    var mPlayers: MutableList<Player> = mutableListOf()
    var mServerID = 0L
    var mMatchID = 0L
    var mShowAdditionalInfo = false
}

data class Player(var steamId: Long, var currentSteamName: String, var officialName: String = "")

class LiveMatchesViewHolder(view: View?, adapter: LiveMatchesAdapter) :
        RecyclerView.ViewHolder(view), LayoutContainer, View.OnClickListener {
    override val containerView: View? = view
    private val mAdapter = adapter

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View?) = mAdapter.switchShowAdditionalInfo(adapterPosition)
}
