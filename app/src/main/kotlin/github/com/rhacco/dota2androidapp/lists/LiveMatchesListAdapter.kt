package github.com.rhacco.dota2androidapp.lists

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import github.com.rhacco.dota2androidapp.R

class LiveMatchesListAdapter(context: Context) : BaseAdapter() {
    val mListItemsData: MutableList<ListItemData> = mutableListOf()
    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int = mListItemsData.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view: View?
        val item: ListItem
        if (convertView == null) {
            view = mInflater.inflate(R.layout.list_live_matches, parent, false)
            item = ListItem(view)
            view.tag = item
        } else {
            view = convertView
            item = view.tag as ListItem
        }

        item.title?.text = mListItemsData[position].mTitle
        item.playersRadiant?.text = mListItemsData[position].mPlayersRadiant
        item.playersDire?.text = mListItemsData[position].mPlayersDire
        return view
    }

    private class ListItem(view: View?) {
        val title: TextView? = view?.findViewById(R.id.title)
        val playersRadiant: TextView? = view?.findViewById(R.id.playersRadiant)
        val playersDire: TextView? = view?.findViewById(R.id.playersDire)
    }

    class ListItemData {
        var mAverageMMR = 0  // Used to sort list by average MMR
        var mMatchID = 0L  // Used to prevent duplicates
        var mTitle: String = ""
        var mPlayersRadiant: String = "Radiant players:"
        var mPlayersDire: String = "Dire players:"
    }

    // We need to override this method but it's never used so we just return anything
    override fun getItem(dummy: Int): Any = -1

    // We need to override this method but it's never used so we just return anything
    override fun getItemId(dummy: Int): Long = -1
}
