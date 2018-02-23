package github.com.rhacco.dotascoop.lists

import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.com.rhacco.dotascoop.App
import github.com.rhacco.dotascoop.R
import github.com.rhacco.dotascoop.activities.HeroInfoActivity
import github.com.rhacco.dotascoop.sources.databases.entities.HeroEntity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_heroes.*

class HeroesAdapter(private val mContext: Context) : RecyclerView.Adapter<HeroesViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mItemsData: List<HeroEntity> = listOf()
    private var mShownItemsData: List<HeroEntity> = listOf()

    fun update(heroes: List<HeroEntity>) {
        mItemsData = heroes
        showAllEntries()
    }

    fun handleSearchQuery(query: String) {
        mShownItemsData = mItemsData.filter {
            it.localized_name.contains(query, true) ||
                    it.attack_type.contains(query, true) ||
                    it.roles.contains(query, true) ||
                    it.primary_attr.contains(query, true)
        }
        notifyDataSetChanged()
    }

    fun showAllEntries() {
        if (mShownItemsData != mItemsData) {
            mShownItemsData = mItemsData
            notifyDataSetChanged()
        }
    }

    fun getHero(position: Int): HeroEntity = mShownItemsData[position]

    override fun getItemCount(): Int = mShownItemsData.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HeroesViewHolder =
            HeroesViewHolder(mInflater.inflate(R.layout.item_heroes, parent, false), this, mContext)

    override fun onBindViewHolder(holder: HeroesViewHolder, position: Int) {
        val iconId = App.instance.resources.getIdentifier(
                "hero_portrait_vert_" + mShownItemsData[position].id,
                "drawable", App.instance.packageName)
        if (iconId > 0) {
            holder.portrait.setImageDrawable(
                    ContextCompat.getDrawable(App.instance.applicationContext, iconId))
        }
        holder.name.text = mShownItemsData[position].localized_name
    }
}

class HeroesViewHolder(
        override val containerView: View?,
        private val mAdapter: HeroesAdapter, private val mContext: Context) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {
    init {
        itemView.setOnClickListener {
            App.sCurrentHeroToDisplay = mAdapter.getHero(adapterPosition)
            mContext.startActivity(Intent(mContext, HeroInfoActivity::class.java))
        }
    }
}