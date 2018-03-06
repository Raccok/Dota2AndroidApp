package github.com.rhacco.dotascoop.lists

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import github.com.rhacco.dotascoop.App
import github.com.rhacco.dotascoop.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_top_matches.*

class TopMatchesAdapter(private val mContext: Context) :
        RecyclerView.Adapter<TopMatchesViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mItemsData: List<TopMatchesItemData> = listOf()
    private var mShownItemsData: List<TopMatchesItemData> = listOf()
    private var mCurrentQuery: String = ""

    fun update(newTopMatches: List<TopMatchesItemData>) {
        newTopMatches.forEach { newTopMatch ->
            mItemsData.forEach { oldTopMatch ->
                if (newTopMatch.matchId == oldTopMatch.matchId) {
                    newTopMatch.showAdditionalInfo = oldTopMatch.showAdditionalInfo
                    newTopMatch.showOfficialNames = oldTopMatch.showOfficialNames
                }
            }
        }
        mItemsData = newTopMatches
        if (mCurrentQuery.isEmpty())
            showAllEntries()
        else
            handleSearchQuery(mCurrentQuery)
    }

    fun showAllEntries() {
        if (mShownItemsData != mItemsData) {
            mShownItemsData = mItemsData
            notifyDataSetChanged()
        }
        mCurrentQuery = ""
    }

    fun handleSearchQuery(query: String) {
        mShownItemsData = mItemsData.filter {
            queryPlayers(it, query) ||
                    it.teamRadiant.contains(query, true) ||
                    it.teamDire.contains(query, true) ||
                    it.heroNames.contains(query, true)
        }
        notifyDataSetChanged()
        mCurrentQuery = query
    }

    private fun queryPlayers(itemData: TopMatchesItemData, query: String): Boolean {
        itemData.players.forEach {
            if (it.officialName.contains(query, true) || it.currentSteamName.contains(query, true))
                return true
        }
        return false
    }

    fun switchShowAdditionalInfo(itemPosition: Int) {
        mShownItemsData[itemPosition].showAdditionalInfo =
                !mShownItemsData[itemPosition].showAdditionalInfo
        notifyItemChanged(itemPosition)
    }

    fun switchShowOfficialNames(itemPosition: Int) {
        mShownItemsData[itemPosition].showOfficialNames =
                !mShownItemsData[itemPosition].showOfficialNames
        notifyItemChanged(itemPosition)
    }

    fun getMatchId(itemPosition: Int) = mShownItemsData[itemPosition].matchId

    override fun getItemCount(): Int = mShownItemsData.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TopMatchesViewHolder =
            TopMatchesViewHolder(
                    mInflater.inflate(R.layout.item_top_matches, parent, false), this, mContext)

    override fun onBindViewHolder(holder: TopMatchesViewHolder, position: Int) {
        val itemData = mShownItemsData[position]
        bindRealtimeStats(holder, itemData)
        holder.team_radiant?.text = itemData.teamRadiant
        holder.team_dire?.text = itemData.teamDire
        if (itemData.isTournamentMatch)
            holder.average_mmr?.visibility = View.GONE
        else {
            holder.average_mmr?.text =
                    App.instance.getString(R.string.average_mmr, itemData.averageMMR)
            holder.average_mmr?.visibility = View.VISIBLE
        }
        bindPlayerName(holder.radiant_player0, itemData.players[0], itemData)
        bindPlayerScore(holder.radiant_player0_score, itemData.players[0].score, itemData)
        bindPlayerName(holder.radiant_player1, itemData.players[1], itemData)
        bindPlayerScore(holder.radiant_player1_score, itemData.players[1].score, itemData)
        bindPlayerName(holder.radiant_player2, itemData.players[2], itemData)
        bindPlayerScore(holder.radiant_player2_score, itemData.players[2].score, itemData)
        bindPlayerName(holder.radiant_player3, itemData.players[3], itemData)
        bindPlayerScore(holder.radiant_player3_score, itemData.players[3].score, itemData)
        bindPlayerName(holder.radiant_player4, itemData.players[4], itemData)
        bindPlayerScore(holder.radiant_player4_score, itemData.players[4].score, itemData)
        bindPlayerName(holder.dire_player0, itemData.players[5], itemData)
        bindPlayerScore(holder.dire_player0_score, itemData.players[5].score, itemData)
        bindPlayerName(holder.dire_player1, itemData.players[6], itemData)
        bindPlayerScore(holder.dire_player1_score, itemData.players[6].score, itemData)
        bindPlayerName(holder.dire_player2, itemData.players[7], itemData)
        bindPlayerScore(holder.dire_player2_score, itemData.players[7].score, itemData)
        bindPlayerName(holder.dire_player3, itemData.players[8], itemData)
        bindPlayerScore(holder.dire_player3_score, itemData.players[8].score, itemData)
        bindPlayerName(holder.dire_player4, itemData.players[9], itemData)
        bindPlayerScore(holder.dire_player4_score, itemData.players[9].score, itemData)
        bindHeroPortrait(holder.radiant_player0_hero_portrait, 0, itemData)
        bindHeroPortrait(holder.radiant_player1_hero_portrait, 1, itemData)
        bindHeroPortrait(holder.radiant_player2_hero_portrait, 2, itemData)
        bindHeroPortrait(holder.radiant_player3_hero_portrait, 3, itemData)
        bindHeroPortrait(holder.radiant_player4_hero_portrait, 4, itemData)
        bindHeroPortrait(holder.dire_player0_hero_portrait, 5, itemData)
        bindHeroPortrait(holder.dire_player1_hero_portrait, 6, itemData)
        bindHeroPortrait(holder.dire_player2_hero_portrait, 7, itemData)
        bindHeroPortrait(holder.dire_player3_hero_portrait, 8, itemData)
        bindHeroPortrait(holder.dire_player4_hero_portrait, 9, itemData)
        bindSpectateInfo(holder, itemData)
        bindPostMatchInfo(holder, itemData)
    }

    private fun bindRealtimeStats(holder: TopMatchesViewHolder, itemData: TopMatchesItemData) {
        if (itemData.heroIds.isNotEmpty()) {
            val goldAdvantageThousands =
                    Math.floor(Math.abs(itemData.goldAdvantage) / 1000.0).toInt()
            val goldAdvantageHundreds = Math.round(Math.abs(itemData.goldAdvantage) / 100.0) % 10
            holder.gold_advantage?.text = App.instance.getString(R.string.gold_advantage,
                    goldAdvantageThousands, goldAdvantageHundreds)
            val params = RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            params.addRule(RelativeLayout.CENTER_VERTICAL)
            params.leftMargin = 25
            params.rightMargin = 25
            if (itemData.goldAdvantage >= 0)
                params.addRule(RelativeLayout.LEFT_OF, R.id.score_radiant)
            else
                params.addRule(RelativeLayout.RIGHT_OF, R.id.score_dire)
            holder.gold_advantage?.layoutParams = params
            holder.gold_advantage?.visibility = View.VISIBLE
        } else
            holder.gold_advantage?.visibility = View.GONE
        holder.score_radiant?.text = itemData.radiantScore.toString()
        var elapsedTimeMin = 0
        var elapsedTimeSec = 0
        if (itemData.heroIds.isNotEmpty() && itemData.elapsedTime > 0) {
            elapsedTimeMin = Math.floor(itemData.elapsedTime / 60.0).toInt()
            elapsedTimeSec = itemData.elapsedTime % 60
        }
        if (elapsedTimeSec < 10)
            holder.elapsed_time?.text = App.instance.getString(R.string.elapsed_time_corrected_sec,
                    elapsedTimeMin, elapsedTimeSec)
        else
            holder.elapsed_time?.text = App.instance.getString(R.string.elapsed_time,
                    elapsedTimeMin, elapsedTimeSec)
        holder.score_dire?.text = itemData.direScore.toString()
    }

    private fun bindPlayerName(textView: TextView?, player: Player, itemData: TopMatchesItemData) {
        if (player.officialName.isNotEmpty() &&
                (itemData.isTournamentMatch || itemData.showOfficialNames)) {
            textView?.text = player.officialName
            if (itemData.isTournamentMatch)
                textView?.setTextColor(ContextCompat.getColor(
                        App.instance.applicationContext, R.color.text_general))
            else
                textView?.setTextColor(ContextCompat.getColor(
                        App.instance.applicationContext, R.color.text_pro_player))
        } else {
            textView?.text = player.currentSteamName
            textView?.setTextColor(ContextCompat.getColor(
                    App.instance.applicationContext, R.color.text_general))
        }
    }

    private fun bindPlayerScore(textView: TextView?, score: String, itemData: TopMatchesItemData) {
        if (itemData.showAdditionalInfo) {
            textView?.text = score
            textView?.visibility = View.VISIBLE
        } else
            textView?.visibility = View.GONE
    }

    private fun bindHeroPortrait(
            imageView: ImageView?, playerIndex: Int, itemData: TopMatchesItemData) {
        if (itemData.showAdditionalInfo && itemData.heroIds.size == 10) {
            val iconId = App.instance.resources.getIdentifier(
                    "hero_portrait_vert_" + itemData.heroIds[playerIndex],
                    "drawable", App.instance.packageName)
            if (iconId > 0) {
                imageView?.setImageDrawable(
                        ContextCompat.getDrawable(App.instance.applicationContext, iconId))
                imageView?.visibility = View.VISIBLE
                return
            }
        }
        imageView?.visibility = View.GONE
    }

    private fun bindSpectateInfo(holder: TopMatchesViewHolder, itemData: TopMatchesItemData) {
        if (itemData.showAdditionalInfo && itemData.spectators >= 0) {
            holder.spectators?.text =
                    App.instance.getString(R.string.spectators, itemData.spectators)
            holder.spectators?.visibility = View.VISIBLE
            holder.spectate_command?.text = App.instance.getString(
                    R.string.spectate_command, itemData.serverId)
            holder.spectate_command?.visibility = View.VISIBLE
        } else {
            holder.spectators?.visibility = View.GONE
            holder.spectate_command?.visibility = View.GONE
        }
    }

    private fun bindPostMatchInfo(holder: TopMatchesViewHolder, itemData: TopMatchesItemData) {
        if (itemData.showAdditionalInfo && itemData.spectators < 0)
            holder.post_match_info?.visibility = View.VISIBLE
        else
            holder.post_match_info?.visibility = View.GONE
    }
}

data class TopMatchesItemData(
        var serverId: Long = 0L, var matchId: Long = 0L, var isTournamentMatch: Boolean = false,
        var spectators: Int = -1,
        var teamRadiant: String = "", var teamDire: String = "", var averageMMR: Int = 0,
        var goldAdvantage: Int = 0, var elapsedTime: Int = -1,
        var radiantScore: Int = 0, var direScore: Int = 0,
        var players: MutableList<Player> = mutableListOf(),
        var heroIds: List<Int> = mutableListOf(), var heroNames: String = "",
        var showAdditionalInfo: Boolean = false, var showOfficialNames: Boolean = true)

data class Player(var currentSteamName: String, var officialName: String, var score: String)

class TopMatchesViewHolder(
        override val containerView: View?,
        private val mAdapter: TopMatchesAdapter, private val mContext: Context) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
    init {
        itemView.setOnClickListener { mAdapter.switchShowAdditionalInfo(adapterPosition) }
        itemView.setOnLongClickListener { mAdapter.switchShowOfficialNames(adapterPosition); true }
        button_opendota.setOnClickListener {
            mContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(
                    "https://www.opendota.com/matches/" + mAdapter.getMatchId(adapterPosition))))
        }
        button_dotabuff.setOnClickListener {
            mContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(
                    "https://www.dotabuff.com/matches/" + mAdapter.getMatchId(adapterPosition))))
        }
    }
}