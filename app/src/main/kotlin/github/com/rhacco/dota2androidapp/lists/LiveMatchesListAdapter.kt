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

    override fun getItem(position: Int): Any = mTitles[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val view: View?
        val row: Row
        if (convertView == null) {
            view = mInflater.inflate(R.layout.list_live_matches, parent, false)
            row = Row(view)
            view.tag = row
        } else {
            view = convertView
            row = view.tag as Row
        }

        row.title?.text = mTitles[position]
        row.playersRadiant?.text = mPlayersRadiant[position]
        row.playersDire?.text = mPlayersDire[position]
        return view
    }

    private class Row(view: View?) {
        val title: TextView? = view?.findViewById(R.id.title)
        val playersRadiant: TextView? = view?.findViewById(R.id.playersRadiant)
        val playersDire: TextView? = view?.findViewById(R.id.playersDire)
    }
}
