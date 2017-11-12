package github.com.rhacco.dota2androidapp.lists

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        if (newItemData.mAverageMMR > 0)
            index = mItemsData.count { it.mAverageMMR < 1 || it.mAverageMMR >= newItemData.mAverageMMR }
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

    fun switchShowOfficialName(itemPosition: Int) {
        mItemsData[itemPosition].mPlayers.forEach { it.showOfficialName = !it.showOfficialName }
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
        if (itemData.mPlayers.size == 10) {
            bindPlayerName(holder.radiant_player0, itemData.mPlayers[0])
            bindPlayerName(holder.radiant_player1, itemData.mPlayers[1])
            bindPlayerName(holder.radiant_player2, itemData.mPlayers[2])
            bindPlayerName(holder.radiant_player3, itemData.mPlayers[3])
            bindPlayerName(holder.radiant_player4, itemData.mPlayers[4])
            bindPlayerName(holder.dire_player0, itemData.mPlayers[5])
            bindPlayerName(holder.dire_player1, itemData.mPlayers[6])
            bindPlayerName(holder.dire_player2, itemData.mPlayers[7])
            bindPlayerName(holder.dire_player3, itemData.mPlayers[8])
            bindPlayerName(holder.dire_player4, itemData.mPlayers[9])
        } else
            Log.d(App.instance.getString(R.string.log_msg_debug),
                    "A Live Matches item has corrupted players data")
    }

    private fun bindPlayerName(textView: TextView?, player: Player) =
            when {
                player.officialName.isEmpty() -> {
                    textView?.text = player.currentSteamName
                    textView?.setTextColor(ContextCompat.getColor(
                            App.instance.applicationContext, R.color.text_general))
                }
                player.showOfficialName -> {
                    textView?.text = player.officialName
                    textView?.setTextColor(ContextCompat.getColor(
                            App.instance.applicationContext, R.color.text_pro_player))
                }
                else -> {
                    textView?.text = player.currentSteamName
                    textView?.setTextColor(ContextCompat.getColor(
                            App.instance.applicationContext, R.color.text_general))
                }
            }
}

class LiveMatchesItemData {
    var mMatchID = 0L
    var mAverageMMR = 0
    var mTitle: String = ""
    var mPlayers: MutableList<Player> = mutableListOf()
}

data class Player(var steamId: Long, var currentSteamName: String, var officialName: String = "",
                  var showOfficialName: Boolean = true)

class LiveMatchesViewHolder(view: View?, adapter: LiveMatchesAdapter) :
        RecyclerView.ViewHolder(view), LayoutContainer, View.OnClickListener {
    override val containerView: View? = view
    private val mAdapter = adapter

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View?) = mAdapter.switchShowOfficialName(adapterPosition)
}
