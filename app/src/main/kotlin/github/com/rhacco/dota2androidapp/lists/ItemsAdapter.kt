package github.com.rhacco.dota2androidapp.lists

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.activities.ItemInfoActivity
import github.com.rhacco.dota2androidapp.sources.databases.entities.ItemEntity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_heroes.*

class ItemsAdapter(private val mContext: Context) : RecyclerView.Adapter<ItemsViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mItemsData: List<ItemEntity> = listOf()
    private var mShownItemsData: List<ItemEntity> = listOf()

    fun update(heroes: List<ItemEntity>) {
        mItemsData = heroes
        showAllEntries()
    }

    fun handleSearchQuery(query: String) {
        mShownItemsData = mItemsData.filter {
            it.dname.contains(query, true) ||
                    (it.components != null && it.components.contains(query, true))
        }
        notifyDataSetChanged()
    }

    fun showAllEntries() {
        if (mShownItemsData != mItemsData) {
            mShownItemsData = mItemsData
            notifyDataSetChanged()
        }
    }

    fun getItem(position: Int): ItemEntity = mShownItemsData[position]

    override fun getItemCount(): Int = mShownItemsData.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ItemsViewHolder =
            ItemsViewHolder(mInflater.inflate(R.layout.item_items, parent, false), this, mContext)

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
        val itemData = mShownItemsData[position]
        val iconId =
                if (itemData.dname.contains("recipe", true))
                    App.instance.resources.getIdentifier(
                            "item_portrait_horiz_recipe", "drawable", App.instance.packageName)
                else
                    App.instance.resources.getIdentifier(
                            "item_portrait_horiz_" + itemData.id,
                            "drawable", App.instance.packageName)
        if (iconId > 0) {
            holder.portrait.setImageDrawable(
                    ContextCompat.getDrawable(App.instance.applicationContext, iconId))
        }
        holder.name.text = itemData.dname
    }
}

class ItemsViewHolder(
        override val containerView: View?,
        private val mAdapter: ItemsAdapter, private val mContext: Context) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
    init {
        itemView.setOnClickListener {
            App.sCurrentItemToDisplay = mAdapter.getItem(adapterPosition)
            mContext.startActivity(Intent(mContext, ItemInfoActivity::class.java))
        }
    }
}