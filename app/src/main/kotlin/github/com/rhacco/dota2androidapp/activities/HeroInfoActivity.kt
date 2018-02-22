package github.com.rhacco.dota2androidapp.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import github.com.rhacco.dota2androidapp.App
import github.com.rhacco.dota2androidapp.R
import github.com.rhacco.dota2androidapp.base.BaseNavigationDrawerActivity
import github.com.rhacco.dota2androidapp.lists.AbilitiesAdapter
import kotlinx.android.synthetic.main.activity_hero_info.*
import java.text.DecimalFormat

class HeroInfoActivity : BaseNavigationDrawerActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hero_info)
        super.initNavigationDrawer(drawer_layout)
        val hero = App.sCurrentHeroToDisplay

        val iconId = resources.getIdentifier("hero_portrait_vert_" + hero.id, "drawable", packageName)
        if (iconId > 0)
            portrait.setImageDrawable(ContextCompat.getDrawable(applicationContext, iconId))
        name.text = hero.localized_name
        basic_keywords.text = hero.primary_attr + " - " + hero.attack_type + " - " + hero.roles
        str_and_str_per_lvl.text = hero.base_str.toString() + " + " +
                DecimalFormat("0.0").format(hero.str_gain + 0.0001) + " per level"
        agi_and_agi_per_lvl.text = hero.base_agi.toString() + " + " +
                DecimalFormat("0.0").format(hero.agi_gain + 0.0001) + " per level"
        int_and_int_per_lvl.text = hero.base_int.toString() + " + " +
                DecimalFormat("0.0").format(hero.int_gain + 0.0001) + " per level"
        health.text = (hero.base_health + hero.base_str * 20).toString()
        var armorVal = DecimalFormat("0.0").format(hero.base_armor + hero.base_agi / 6.0)
        if (armorVal == "-0.0")
            armorVal = "0.0"
        armor.text = armorVal
        mana.text = (hero.base_mana + hero.base_int * 12).toString()
        var attackMin = hero.base_attack_min
        var attackMax = hero.base_attack_max
        when (hero.primary_attr) {
            "Strength" -> {
                attackMin += hero.base_str; attackMax += hero.base_str
            }
            "Agility" -> {
                attackMin += hero.base_agi; attackMax += hero.base_agi
            }
            else -> {
                attackMin += hero.base_int; attackMax += hero.base_int
            }
        }
        damage.text = "Damage: $attackMin - $attackMax"
        attack_rate.text = "Attack Rate: " + hero.attack_rate
        attack_range.text = "Attack Range: " + hero.attack_range
        if (hero.attack_type == "Ranged")
            projectile_speed.text = "Projectile Speed: " + hero.projectile_speed
        else
            projectile_speed.visibility = View.GONE
        magic_resistance.text = "Magic Resistance: " + hero.base_mr + "%"
        movement_speed.text = "Movement Speed: " + hero.move_speed
        turn_rate.text = "Turn Rate: " + hero.turn_rate

        val abilitiesAdapter = AbilitiesAdapter(this)
        abilities.adapter = abilitiesAdapter
        abilities.layoutManager = LinearLayoutManager(this)
        abilities.isNestedScrollingEnabled = false
        abilitiesAdapter.update(hero.abilities)

        if (hero.talents.size == 8) {
            talent0.text = hero.talents[0]
            talent1.text = hero.talents[1]
            talent2.text = hero.talents[2]
            talent3.text = hero.talents[3]
            talent4.text = hero.talents[4]
            talent5.text = hero.talents[5]
            talent6.text = hero.talents[6]
            talent7.text = hero.talents[7]
        } else
            Log.d(getString(R.string.log_msg_debug),
                    "Hero " + hero.localized_name + " does not have exactly 8 talents")
    }
}