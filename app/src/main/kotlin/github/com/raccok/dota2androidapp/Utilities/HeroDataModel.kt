package github.com.raccok.dota2androidapp.Utilities

// A model to represent and store Dota 2 hero data received from the official Dota 2 API.
// For more information about the format, see https://wiki.teamfortress.com/wiki/WebAPI/GetHeroes
object HeroDataModel {
  data class Result(val result: Heroes)
  data class Heroes(private val heroes: List<HeroData>) {
    fun heroNames() : List<String> {
      val names: MutableList<String> = mutableListOf()
      heroes.mapTo(names) { it.name }
      return names.toList()
    }
    fun heroIds() : List<Int> {
      val ids: MutableList<Int> = mutableListOf()
      heroes.mapTo(ids) { it.id }
      return ids.toList()
    }
    fun heroNamesLocalized() : List<String> {
      val localizedNames: MutableList<String> = mutableListOf()
      heroes.mapTo(localizedNames) { it.localized_name }
      return localizedNames.toList()
    }
  }
  data class HeroData(val name: String, val id: Int, val localized_name: String)
}
