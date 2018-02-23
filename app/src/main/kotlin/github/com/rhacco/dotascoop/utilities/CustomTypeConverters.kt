package github.com.rhacco.dotascoop.utilities

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import github.com.rhacco.dotascoop.sources.databases.entities.Ability
import github.com.rhacco.dotascoop.sources.databases.entities.AbilityAttribute
import github.com.rhacco.dotascoop.sources.databases.entities.ItemAttribute

class CustomTypeConverters {
    private val mGson = Gson()
    private val mDivider1 = "#_#"
    private val mDivider2 = "~_~"
    private val mDivider3 = "§_§"
    private val mDivider4 = "%_%"

    @TypeConverter
    fun stringListToString(strings: List<String>): String = mGson.toJson(strings)

    @TypeConverter
    fun stringToStringList(string: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return mGson.fromJson(string, listType)
    }

    @TypeConverter
    fun abilityListToString(abilities: List<Ability>): String {
        var string = ""
        abilities.forEach {
            string += it.dname + mDivider1 + it.desc + mDivider1 + it.behavior +
                    mDivider1 + it.dmg_type + mDivider1 + it.bkbpierce + mDivider1 +
                    it.cd + mDivider1 + it.mc + mDivider1 + abilityAttributesToString(it.attrib) +
                    mDivider2
        }
        return string.removeSuffix(mDivider2)
    }

    @TypeConverter
    fun stringToAbilityList(string: String): List<Ability> {
        val abilities: MutableList<Ability> = mutableListOf()
        val entries = string.split(mDivider2)
        entries.forEach {
            val values = it.split(mDivider1)
            abilities.add(Ability(
                    values[0], values[1], values[2], values[3], values[4],
                    values[5], values[6], stringToAbilityAttributes(values[7])))
        }
        return abilities
    }

    private fun abilityAttributesToString(attributes: List<AbilityAttribute>): String {
        if (attributes.isEmpty())
            return " "
        var string = ""
        attributes.forEach { string += it.header + mDivider3 + it.value + mDivider4 }
        return string.removeSuffix(mDivider4)
    }

    private fun stringToAbilityAttributes(string: String): List<AbilityAttribute> {
        val attributes: MutableList<AbilityAttribute> = mutableListOf()
        if (string == " ")
            return attributes
        val entries = string.split(mDivider4)
        entries.forEach {
            val values = it.split(mDivider3)
            attributes.add(AbilityAttribute(values[0], values[1]))
        }
        return attributes
    }

    @TypeConverter
    fun itemAttributesToString(attributes: List<ItemAttribute>): String {
        if (attributes.isEmpty())
            return " "
        var string = ""
        attributes.forEach {
            string += it.header + mDivider1 + it.value + mDivider1 + it.footer + mDivider2
        }
        return string.removeSuffix(mDivider2)
    }

    @TypeConverter
    fun stringToItemAttributes(string: String): List<ItemAttribute> {
        val attributes: MutableList<ItemAttribute> = mutableListOf()
        if (string == " ")
            return attributes
        val entries = string.split(mDivider2)
        entries.forEach {
            val values = it.split(mDivider1)
            attributes.add(ItemAttribute(values[0], values[1], values[2]))
        }
        return attributes
    }
}