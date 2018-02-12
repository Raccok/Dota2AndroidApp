package github.com.rhacco.dota2androidapp.lists

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.sources.databases.entities.Ability
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_abilities.*

class AbilitiesAdapter(context: Context) : RecyclerView.Adapter<AbilitiesViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(context)
    private var mItemsData: List<Ability> = listOf()

    fun update(abilities: List<Ability>) {
        mItemsData = abilities
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mItemsData.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AbilitiesViewHolder =
            AbilitiesViewHolder(mInflater.inflate(R.layout.item_abilities, parent, false))

    override fun onBindViewHolder(holder: AbilitiesViewHolder, position: Int) {
        holder.name.text = mItemsData[position].dname
        holder.description.text = mItemsData[position].desc
    }
}

class AbilitiesViewHolder(override val containerView: View?) :
        RecyclerView.ViewHolder(containerView), LayoutContainer