package github.com.rhacco.dota2androidapp.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
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

        portrait.setImageDrawable(ContextCompat.getDrawable(applicationContext,
                resources.getIdentifier("hero_portrait_vert_" + hero.id, "drawable", packageName)))
        name.text = hero.localized_name
        var basicKeywords = when (hero.primary_attr) {
            "str" -> "Strength"
            "agi" -> "Agility"
            else -> "Intelligence"
        }
        basicKeywords += " - " + hero.attack_type + " - "
        hero.roles.forEach { basicKeywords += it + ", " }
        basic_keywords.text = basicKeywords.removeSuffix(", ")
        strength.text = "Str:\t" + hero.base_str + " + " + hero.str_gain + " per level"
        agility.text = "Agi:\t" + hero.base_agi + " + " + hero.agi_gain + " per level"
        intelligence.text = "Int:\t\t" + hero.base_int + " + " + hero.int_gain + " per level"
        health.text = "Health:\t" + (hero.base_health + hero.base_str * 20)
        armor.text = "Armor:\t" +
                DecimalFormat("#.0").format(hero.base_armor + hero.base_agi * 0.16470588235)
        mana.text = "Mana:\t\t" + (hero.base_mana + hero.base_int * 12)
        var attackMin = hero.base_attack_min
        var attackMax = hero.base_attack_max
        when (hero.primary_attr) {
            "str" -> {
                attackMin += hero.base_str; attackMax += hero.base_str
            }
            "agi" -> {
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
    }
}