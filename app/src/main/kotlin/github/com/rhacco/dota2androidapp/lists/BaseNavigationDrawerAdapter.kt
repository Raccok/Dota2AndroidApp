package github.com.rhacco.dota2androidapp.lists

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.activities.MainActivity
import github.com.rhacco.dota2androidapp.activities.TopMatchesActivity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_navigation_drawer.*

class BaseNavigationDrawerAdapter(context: Context) :
        RecyclerView.Adapter<BaseNavigationDrawerViewHolder>() {
    private val mContext: Context = context
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private val mActivityNames: MutableList<String> = mutableListOf()

    init {
        mActivityNames.add(App.instance.getString(R.string.activity_main))
        mActivityNames.add(App.instance.getString(R.string.activity_top_matches))
    }

    fun switchToActivity(itemPosition: Int) {
        var intent = Intent()
        when (itemPosition) {
            0 -> intent = Intent(mContext, MainActivity::class.java)
            1 -> intent = Intent(mContext, TopMatchesActivity::class.java)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        mContext.startActivity(intent)
    }

    override fun getItemCount(): Int = mActivityNames.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BaseNavigationDrawerViewHolder =
            BaseNavigationDrawerViewHolder(mInflater.inflate(R.layout.item_navigation_drawer,
                    parent, false), this)

    override fun onBindViewHolder(holder: BaseNavigationDrawerViewHolder, position: Int) {
        holder.activity_name?.text = mActivityNames[position]
    }
}

class BaseNavigationDrawerViewHolder(view: View?, adapter: BaseNavigationDrawerAdapter) :
        RecyclerView.ViewHolder(view), LayoutContainer, View.OnClickListener {
    override val containerView: View? = view
    private val mAdapter = adapter

    init {
        itemView.setOnClickListener(this)
    }

    override fun onClick(view: View?) = mAdapter.switchToActivity(adapterPosition)
}