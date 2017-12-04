package github.com.rhacco.dota2androidapp.sources.repos

object HeroesRepository {
    fun getNamesByIds(ids: List<Int>): List<String?> =
            HeroesLocalDataSource.getNamesByIds(ids)
}