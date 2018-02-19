package github.com.rhacco.dota2androidapp.lists

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.sources.databases.entities.Ability
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_abilities.*

class AbilitiesAdapter(private val mContext: Context) : RecyclerView.Adapter<AbilitiesViewHolder>() {
    private val mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mItemsData: List<Ability> = listOf()

    fun update(abilities: List<Ability>) {
        mItemsData = abilities
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = mItemsData.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AbilitiesViewHolder =
            AbilitiesViewHolder(mInflater.inflate(R.layout.item_abilities, parent, false))

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AbilitiesViewHolder, position: Int) {
        val itemData = mItemsData[position]
        holder.name.text = itemData.dname
        holder.name.setOnClickListener { showDescription(itemData.dname, itemData.desc) }
        holder.info.setOnClickListener { showDescription(itemData.dname, itemData.desc) }
        holder.behavior.text = "Ability: " + itemData.behavior
        if (itemData.dmg_type != null && itemData.dmg_type != "null")
            holder.dmg_type.text = "Damage Type: " + itemData.dmg_type
        else
            holder.dmg_type.visibility = View.GONE
        if (itemData.bkbpierce != null && itemData.bkbpierce != "null")
            holder.pierces_spell_immunity.text = "Pierces Spell Immunity: " + itemData.bkbpierce
        else
            holder.pierces_spell_immunity.visibility = View.GONE
        if (itemData.cd != null && itemData.cd != "null")
            holder.cooldown.text = "Cooldown: " + itemData.cd
        else
            holder.cooldown.visibility = View.GONE
        if (itemData.mc != null && itemData.mc != "null")
            holder.mana_cost.text = "Mana Cost: " + itemData.mc
        else
            holder.mana_cost.visibility = View.GONE
        if (itemData.attrib.isNotEmpty()) {
            var attributes = ""
            itemData.attrib.forEach { attributes += it.header + " " + it.value + "\n" }
            holder.attributes.text = attributes.removeSuffix("\n")
        } else
            holder.attributes.visibility = View.GONE
    }

    private fun showDescription(ability: String, description: String) {
        val alertDialog = AlertDialog.Builder(
                ContextThemeWrapper(mContext, R.style.AlertDialogTheme)).create()
        alertDialog.setTitle(ability)
        alertDialog.setMessage(description)
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, App.instance.getString(R.string.dialog_ok),
                { dialog, _ -> dialog.dismiss() })
        alertDialog.show()
    }
}

class AbilitiesViewHolder(override val containerView: View?) :
        RecyclerView.ViewHolder(containerView), LayoutContainer