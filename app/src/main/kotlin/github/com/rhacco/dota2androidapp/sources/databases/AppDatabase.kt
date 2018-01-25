package github.com.rhacco.dota2androidapp.sources.databases

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import github.com.rhacco.dota2androidapp.api.LeaderboardsResponse
import github.com.rhacco.dota2androidapp.sources.databases.daos.LeaderboardsDao
import github.com.rhacco.dota2androidapp.sources.databases.entities.*
import io.reactivex.Single

@Database(entities = [(LeaderboardEntryAmericas::class), (LeaderboardEntryEurope::class),
    (LeaderboardEntrySEAsia::class), (LeaderboardEntryChina::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun leaderboardsDao(): LeaderboardsDao

    fun getLeaderboard(region: String): Single<List<LeaderboardsResponse.Entry>> =
            when (region) {
                "americas" -> {
                    Single.create(
                            { subscriber ->
                                leaderboardsDao().getLeaderboardAmericas().subscribe(
                                        { result -> subscriber.onSuccess(convert(result)) },
                                        { error -> subscriber.onError(error) }
                                )
                            }
                    )
                }
                "europe" -> {
                    Single.create(
                            { subscriber ->
                                leaderboardsDao().getLeaderboardEurope().subscribe(
                                        { result -> subscriber.onSuccess(convert(result)) },
                                        { error -> subscriber.onError(error) }
                                )
                            }
                    )
                }
                "se_asia" -> {
                    Single.create(
                            { subscriber ->
                                leaderboardsDao().getLeaderboardSEAsia().subscribe(
                                        { result -> subscriber.onSuccess(convert(result)) },
                                        { error -> subscriber.onError(error) }
                                )
                            }
                    )
                }
                "china" -> {
                    Single.create(
                            { subscriber ->
                                leaderboardsDao().getLeaderboardChina().subscribe(
                                        { result -> subscriber.onSuccess(convert(result)) },
                                        { error -> subscriber.onError(error) }
                                )
                            }
                    )
                }
                else -> {
                    Single.create({ subscriber ->
                        subscriber.onError(Throwable("Failed to get leaderboard '$region' " +
                                "from database: Invalid parameter 'region'"))
                    })
                }
            }

    fun storeLeaderboard(region: String, leaderboard: List<LeaderboardsResponse.Entry>) {
        when (region) {
            "americas" -> {
                val converted: MutableList<LeaderboardEntryAmericas> = mutableListOf()
                leaderboard.forEach {
                    converted.add(LeaderboardEntryAmericas(it.rank, it.name, it.last_rank))
                }
                leaderboardsDao().storeLeaderboardAmericas(converted)
            }
            "europe" -> {
                val converted: MutableList<LeaderboardEntryEurope> = mutableListOf()
                leaderboard.forEach {
                    converted.add(LeaderboardEntryEurope(it.rank, it.name, it.last_rank))
                }
                leaderboardsDao().storeLeaderboardEurope(converted)
            }
            "se_asia" -> {
                val converted: MutableList<LeaderboardEntrySEAsia> = mutableListOf()
                leaderboard.forEach {
                    converted.add(LeaderboardEntrySEAsia(it.rank, it.name, it.last_rank))
                }
                leaderboardsDao().storeLeaderboardSEAsia(converted)
            }
            "china" -> {
                val converted: MutableList<LeaderboardEntryChina> = mutableListOf()
                leaderboard.forEach {
                    converted.add(LeaderboardEntryChina(it.rank, it.name, it.last_rank))
                }
                leaderboardsDao().storeLeaderboardChina(converted)
            }
        }
    }

    private fun convert(leaderboard: List<LeaderboardEntryEntity>): List<LeaderboardsResponse.Entry> {
        val converted: MutableList<LeaderboardsResponse.Entry> = mutableListOf()
        leaderboard.forEach { converted.add(LeaderboardsResponse.Entry(it.rank, it.name, it.last_rank)) }
        return converted
    }

    companion object {
        const val NAME = "app-db"
    }
}

fun createAppDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.NAME).build()