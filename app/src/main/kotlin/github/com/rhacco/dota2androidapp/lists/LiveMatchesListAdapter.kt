package github.com.rhacco.dota2androidapp.lists

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import github.com.rhacco.dota2androidapp.R

class LiveMatchesListAdapter(context: Context) : BaseAdapter() {
    val mTitles: MutableList<String> = mutableListOf()
    val mPlayersRadiant: MutableList<String> = mutableListOf()
    val mPlayersDire: MutableList<String> = mutableListOf()
    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int = mTitles.size

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

        item.title?.text = mTitles[position]
        item.playersRadiant?.text = mPlayersRadiant[position]
        item.playersDire?.text = mPlayersDire[position]
        return view
    }

    private class ListItem(view: View?) {
        val title: TextView? = view?.findViewById(R.id.title)
        val playersRadiant: TextView? = view?.findViewById(R.id.playersRadiant)
        val playersDire: TextView? = view?.findViewById(R.id.playersDire)
    }

    // We need to override this method but it's never used so we just return anything
    override fun getItem(dummy: Int): Any = -1

    // We need to override this method but it's never used so we just return anything
    override fun getItemId(dummy: Int): Long = -1
}
