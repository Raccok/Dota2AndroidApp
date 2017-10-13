package github.com.rhacco.dota2androidapp.lists

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import github.com.rhacco.dota2androidapp.R

class LiveMatchesListAdapter(context: Context) : RecyclerView.Adapter<LiveMatchesListAdapter.ListItem>() {
    val mListItemsData: MutableList<ListItemData> = mutableListOf()
    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getItemCount(): Int = mListItemsData.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ListItem {
        val view: View = mInflater.inflate(R.layout.list_live_matches, parent, false)
        return ListItem(view)
    }

    override fun onBindViewHolder(item: ListItem, position: Int) {
        item.title?.text = mListItemsData[position].mTitle
        item.playersRadiant?.text = mListItemsData[position].mPlayersRadiant
        item.playersDire?.text = mListItemsData[position].mPlayersDire
    }

    class ListItem(view: View?) : RecyclerView.ViewHolder(view) {
        val title: TextView? = view?.findViewById(R.id.title)
        val playersRadiant: TextView? = view?.findViewById(R.id.playersRadiant)
        val playersDire: TextView? = view?.findViewById(R.id.playersDire)
    }

    class ListItemData {
        var mAverageMMR = 0  // Used to sort list by average MMR
        var mTitle: String = ""
        var mPlayersRadiant: String = "Radiant players:"
        var mPlayersDire: String = "Dire players:"
    }
}
