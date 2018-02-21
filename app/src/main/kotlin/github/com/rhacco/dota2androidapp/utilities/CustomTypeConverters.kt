package github.com.rhacco.dota2androidapp.utilities

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import github.com.rhacco.dota2androidapp.sources.databases.entities.Ability
import github.com.rhacco.dota2androidapp.sources.databases.entities.Attribute

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
                    it.cd + mDivider1 + it.mc + mDivider1 + attributesToString(it.attrib) +
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
                    values[5], values[6], stringToAttributes(values[7])))
        }
        return abilities
    }

    private fun attributesToString(attributes: List<Attribute>): String {
        if (attributes.isEmpty())
            return " "
        var string = ""
        attributes.forEach { string += it.header + mDivider3 + it.value + mDivider4 }
        return string.removeSuffix(mDivider4)
    }

    private fun stringToAttributes(string: String): List<Attribute> {
        val attributes: MutableList<Attribute> = mutableListOf()
        if (string == " ")
            return attributes
        val entries = string.split(mDivider4)
        entries.forEach {
            val values = it.split(mDivider3)
            attributes.add(Attribute(values[0], values[1]))
        }
        return attributes
    }
}