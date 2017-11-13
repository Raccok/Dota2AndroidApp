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
import github.com.rhacco.dota2androidapp.api.TopLiveGamesResponse
import github.com.rhacco.dota2androidapp.entities.HeroEntity
import github.com.rhacco.dota2androidapp.entities.ProPlayerEntity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_live_matches.*

class LiveMatchesAdapter(context: Context) : RecyclerView.Adapter<LiveMatchesViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val mItemsData: MutableList<LiveMatchesItemData> = mutableListOf()

    // Add tournament matches first, then sort by average MMR (descending)
    fun add(newItemData: LiveMatchesItemData): Boolean {
        // Don't add if match already exists
        mItemsData.filter { newItemData.mMatchId == it.mMatchId }.forEach { return false }

        var index = 0
        if (!newItemData.mIsTournamentMatch)
            index = mItemsData.count {
                it.mIsTournamentMatch || it.mMatchBaseVals.average_mmr >= newItemData.mMatchBaseVals.average_mmr
            }
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

    fun updateHeroNames(matchId: Long, heroEntities: List<HeroEntity>, elapsedTime: Int) {
        if (elapsedTime < 0 || heroEntities.size != 10)
            return
        var index = 0
        while (index < mItemsData.size) {
            if (mItemsData[index].mMatchId == matchId) {
                var itemChanged = false
                mItemsData[index].mHeroes.forEach { hero ->
                    heroEntities.forEach {
                        if (hero.id == it.id && hero.name != it.localized_name) {
                            hero.name = it.localized_name!!
                            itemChanged = true
                        }
                    }
                }
                if (itemChanged)
                    notifyItemChanged(index)
                return
            }
            ++index
        }
    }

    fun updateRealtimeStats(newItemData: LiveMatchesItemData) {
        if (newItemData.mElapsedTime < 0)
            return
        var index = 0
        while (index < mItemsData.size) {
            if (mItemsData[index].mMatchId == newItemData.mMatchId) {
                val oldItemData = mItemsData[index]
                if (oldItemData.mGoldAdvantage != newItemData.mGoldAdvantage ||
                        oldItemData.mRadiantScore != newItemData.mRadiantScore ||
                        oldItemData.mElapsedTime != newItemData.mElapsedTime ||
                        oldItemData.mDireScore != newItemData.mDireScore) {
                    newItemData.mPlayers = oldItemData.mPlayers
                    newItemData.mShowAdditionalInfo = oldItemData.mShowAdditionalInfo
                    mItemsData[index] = newItemData
                    notifyItemChanged(index)
                }
                return
            }
            ++index
        }
    }

    fun getItemsData(): MutableList<LiveMatchesItemData> = mItemsData

    fun remove(matchId: Long) {
        var index = 0
        while (index < mItemsData.size) {
            if (mItemsData[index].mMatchId == matchId) {
                mItemsData.removeAt(index)
                notifyItemRemoved(index)
                return
            }
            ++index
        }
    }

    fun switchShowAdditionalInfo(itemPosition: Int) {
        mItemsData[itemPosition].mShowAdditionalInfo = !mItemsData[itemPosition].mShowAdditionalInfo
        notifyItemChanged(itemPosition)
    }

    override fun getItemCount(): Int = mItemsData.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): LiveMatchesViewHolder =
            LiveMatchesViewHolder(mInflater.inflate(R.layout.item_live_matches, parent, false), this)

    override fun onBindViewHolder(holder: LiveMatchesViewHolder, position: Int) {
        val itemData = mItemsData[position]
        holder.title?.text = itemData.mTitle
        if (itemData.mIsTournamentMatch) {
            holder.team_radiant?.text = itemData.mMatchBaseVals.team_name_radiant
            holder.team_dire?.text = itemData.mMatchBaseVals.team_name_dire
        } else {
            holder.team_radiant?.text = App.instance.getString(R.string.team_radiant)
            holder.team_dire?.text = App.instance.getString(R.string.team_dire)
        }
        bindStats(holder, itemData)
        if (itemData.mPlayers.size == 10) {
            bindPlayerName(holder.radiant_player0, itemData.mPlayers[0], itemData)
            bindHeroName(holder.radiant_player0_hero_name, itemData.mHeroes[0], itemData.mElapsedTime)
            bindPlayerName(holder.radiant_player1, itemData.mPlayers[1], itemData)
            bindHeroName(holder.radiant_player1_hero_name, itemData.mHeroes[1], itemData.mElapsedTime)
            bindPlayerName(holder.radiant_player2, itemData.mPlayers[2], itemData)
            bindHeroName(holder.radiant_player2_hero_name, itemData.mHeroes[2], itemData.mElapsedTime)
            bindPlayerName(holder.radiant_player3, itemData.mPlayers[3], itemData)
            bindHeroName(holder.radiant_player3_hero_name, itemData.mHeroes[3], itemData.mElapsedTime)
            bindPlayerName(holder.radiant_player4, itemData.mPlayers[4], itemData)
            bindHeroName(holder.radiant_player4_hero_name, itemData.mHeroes[4], itemData.mElapsedTime)
            bindPlayerName(holder.dire_player0, itemData.mPlayers[5], itemData)
            bindHeroName(holder.dire_player0_hero_name, itemData.mHeroes[5], itemData.mElapsedTime)
            bindPlayerName(holder.dire_player1, itemData.mPlayers[6], itemData)
            bindHeroName(holder.dire_player1_hero_name, itemData.mHeroes[6], itemData.mElapsedTime)
            bindPlayerName(holder.dire_player2, itemData.mPlayers[7], itemData)
            bindHeroName(holder.dire_player2_hero_name, itemData.mHeroes[7], itemData.mElapsedTime)
            bindPlayerName(holder.dire_player3, itemData.mPlayers[8], itemData)
            bindHeroName(holder.dire_player3_hero_name, itemData.mHeroes[8], itemData.mElapsedTime)
            bindPlayerName(holder.dire_player4, itemData.mPlayers[9], itemData)
            bindHeroName(holder.dire_player4_hero_name, itemData.mHeroes[9], itemData.mElapsedTime)
        } else
            Log.d(App.instance.getString(R.string.log_msg_debug),
                    "A Live Matches item has corrupted players data")
        bindIds(holder, itemData)
    }

    private fun bindStats(holder: LiveMatchesViewHolder, itemData: LiveMatchesItemData) {
        val goldAdvantageThousands = Math.floor(Math.abs(itemData.mGoldAdvantage) / 1000.0).toInt()
        val goldAdvantageHundreds = Math.round(Math.abs(itemData.mGoldAdvantage) / 100.0) % 10
        holder.gold_advantage?.text = App.instance.getString(R.string.gold_advantage,
                goldAdvantageThousands, goldAdvantageHundreds)
        val params = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        params.addRule(RelativeLayout.CENTER_VERTICAL)
        params.leftMargin = 15
        params.rightMargin = 15
        if (itemData.mGoldAdvantage >= 0)
            params.addRule(RelativeLayout.LEFT_OF, R.id.score_radiant)
        else
            params.addRule(RelativeLayout.RIGHT_OF, R.id.score_dire)
        holder.gold_advantage?.layoutParams = params
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

    private fun bindHeroName(textView: TextView?, hero: Hero, elapsedTime: Int) =
            when {
                elapsedTime <= 0 -> textView?.visibility = View.GONE
                else -> {
                    textView?.text = hero.name
                    textView?.visibility = View.VISIBLE
                }
            }

    private fun bindIds(holder: LiveMatchesViewHolder, itemData: LiveMatchesItemData) =
            when {
                itemData.mShowAdditionalInfo -> {
                    holder.server_id?.text = App.instance.getString(
                            R.string.additional_info_server_id, itemData.mServerId)
                    holder.server_id?.visibility = View.VISIBLE
                    holder.match_id?.text = App.instance.getString(
                            R.string.additional_info_match_id, itemData.mMatchId)
                    holder.match_id?.visibility = View.VISIBLE
                }
                else -> {
                    holder.server_id?.visibility = View.GONE
                    holder.match_id?.visibility = View.GONE
                }
            }
}

class LiveMatchesItemData {
    var mMatchBaseVals: TopLiveGamesResponse.Game = TopLiveGamesResponse.Game(0L, 0, "", "")
    var mIsTournamentMatch = false
    var mTitle: String = App.instance.getString(R.string.heading_live_tournament_match)
    var mGoldAdvantage = 0
    var mRadiantScore = 0
    var mElapsedTime = -1
    var mDireScore = 0
    var mPlayers: MutableList<Player> = mutableListOf()
    var mHeroes: MutableList<Hero> = mutableListOf()
    var mServerId = 0L
    var mMatchId = 0L
    var mShowAdditionalInfo = false
}

data class Player(var steamId: Long, var currentSteamName: String, var officialName: String = "")
data class Hero(var id: Int, var name: String = "")  // TODO add hero image here later

class LiveMatchesViewHolder(view: View?, adapter: LiveMatchesAdapter) :
        RecyclerView.ViewHolder(view), LayoutContainer, View.OnClickListener {
    override val containerView: View? = view
    private val mAdapter = adapter

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View?) = mAdapter.switchShowAdditionalInfo(adapterPosition)
}
