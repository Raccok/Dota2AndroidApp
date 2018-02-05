package github.com.rhacco.dota2androidapp.lists

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.sources.databases.entities.HeroEntity
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_heroes.*

class HeroesAdapter(context: Context) : RecyclerView.Adapter<HeroesViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mItemsData: List<HeroEntity> = listOf()

    fun update(heroes: List<HeroEntity>) {
        mItemsData = heroes
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mItemsData.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HeroesViewHolder =
            HeroesViewHolder(mInflater.inflate(R.layout.item_heroes, parent, false))

    override fun onBindViewHolder(holder: HeroesViewHolder, position: Int) {
        val iconId = App.instance.resources.getIdentifier(
                "hero_portrait_vert_" + mItemsData[position].id,
                "drawable", App.instance.packageName)
        if (iconId > 0) {
            holder.portrait.setImageDrawable(
                    ContextCompat.getDrawable(App.instance.applicationContext, iconId))
        }
        holder.name.text = mItemsData[position].localized_name
    }
}

class HeroesViewHolder(override val containerView: View?) :
        RecyclerView.ViewHolder(containerView), LayoutContainer