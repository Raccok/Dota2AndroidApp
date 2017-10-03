package github.com.rhacco.dota2androidapp.sources.repos.matches

import github.com.rhacco.dota2androidapp.entities.RealtimeStatsEntity
import github.com.rhacco.dota2androidapp.sources.db.DatabaseCreator
import io.reactivex.Single

object RealtimeStatsLocalDataSource : RealtimeStatsDataSource {
    private val mRealtimeStatsDao = DatabaseCreator.mDatabase.realtimeStatsDao()

    override fun getRealtimeStats(serverSteamId: Long): Single<List<RealtimeStatsEntity>> =
            mRealtimeStatsDao.loadRealtimeStats(serverSteamId).firstOrError().doOnSuccess { if (it.isEmpty()) throw Exception() }

    override fun updateRealtimeStats(entry: List<RealtimeStatsEntity>) = mRealtimeStatsDao.insert(entry)
}
