package github.com.rhacco.dotascoop.sources.databases.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import github.com.rhacco.dotascoop.sources.databases.entities.LeaderboardEntryAmericas
import github.com.rhacco.dotascoop.sources.databases.entities.LeaderboardEntryChina
import github.com.rhacco.dotascoop.sources.databases.entities.LeaderboardEntryEurope
import github.com.rhacco.dotascoop.sources.databases.entities.LeaderboardEntrySEAsia
import io.reactivex.Flowable

@Dao
interface LeaderboardsDao {
    @Query("SELECT * FROM leaderboard_americas")
    fun getLeaderboardAmericas(): Flowable<List<LeaderboardEntryAmericas>>

    @Query("SELECT * FROM leaderboard_europe")
    fun getLeaderboardEurope(): Flowable<List<LeaderboardEntryEurope>>

    @Query("SELECT * FROM leaderboard_se_asia")
    fun getLeaderboardSEAsia(): Flowable<List<LeaderboardEntrySEAsia>>

    @Query("SELECT * FROM leaderboard_china")
    fun getLeaderboardChina(): Flowable<List<LeaderboardEntryChina>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun storeLeaderboardAmericas(leaderboard: List<LeaderboardEntryAmericas>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun storeLeaderboardEurope(leaderboard: List<LeaderboardEntryEurope>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun storeLeaderboardSEAsia(leaderboard: List<LeaderboardEntrySEAsia>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun storeLeaderboardChina(leaderboard: List<LeaderboardEntryChina>)
}