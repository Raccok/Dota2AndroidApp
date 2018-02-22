package github.com.rhacco.dota2androidapp.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.view.ContextThemeWrapper
import android.view.View
import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.base.BaseNavigationDrawerActivity
import kotlinx.android.synthetic.main.activity_item_info.*

class ItemInfoActivity : BaseNavigationDrawerActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_info)
        super.initNavigationDrawer(drawer_layout)
        val item = App.sCurrentItemToDisplay

        val iconId =
                if (item.dname.contains("recipe", true))
                    resources.getIdentifier("item_portrait_horiz_recipe", "drawable", packageName)
                else
                    resources.getIdentifier("item_portrait_horiz_" + item.id, "drawable", packageName)
        if (iconId > 0)
            portrait.setImageDrawable(ContextCompat.getDrawable(applicationContext, iconId))
        name.text = item.dname
        if (item.cost > 0)
            cost.text = "Gold Cost: " + item.cost
        if (item.attrib.isNotEmpty()) {
            var attributesVal = ""
            item.attrib.forEach {
                if (!it.header.contains("tooltip", true) && it.header != "Var Type:" &&
                        it.value != "0") {
                    attributesVal += it.header + " " + it.value
                    if (it.footer != null && it.footer != "null")
                        attributesVal += " " + it.footer
                    attributesVal += "\n"
                }
            }
            attributes.text = attributesVal.removeSuffix("\n")
        } else
            attributes.visibility = View.GONE
        if (item.mc != null)
            mana_cost.text = "Mana Cost: " + item.mc
        else
            mana_cost.visibility = View.GONE
        if (item.cd != null)
            cooldown.text = "Cooldown: " + item.cd
        else
            cooldown.visibility = View.GONE
        if (item.desc.isNotEmpty())
            description.text = item.desc
        else
            description.visibility = View.GONE
        if (item.notes.isNotEmpty()) {
            notes.setOnClickListener { showNotes(item.notes) }
            info.setOnClickListener { showNotes(item.notes) }
        } else {
            notes.visibility = View.GONE
            info.visibility = View.GONE
        }
        if (item.lore.isNotEmpty())
            lore.text = item.lore
        else
            lore.visibility = View.GONE
    }

    private fun showNotes(notes: String) {
        val alertDialog = AlertDialog.Builder(
                ContextThemeWrapper(this, R.style.AlertDialogTheme)).create()
        alertDialog.setMessage(notes)
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.dialog_ok),
                { dialog, _ -> dialog.dismiss() })
        alertDialog.show()
    }
}