package github.com.rhacco.dota2androidapp.sources.repos

import io.reactivex.Single

object CustomAPILocalDataSource {
    private var mHeroNames: Map<Int, String> = mapOf()

    // TODO: have the names stored locally like portraits. initially this generates 10 API calls
    fun getHeroNamesByIds(ids: List<Int>): Single<List<String>> =
            Single.create(
                    { subscriber ->
                        val resultNames: MutableList<String> = mutableListOf()
                        if (mHeroNames.isEmpty()) {
                            CustomAPIRemoteDataSource.getHeroNames()
                                    .subscribe(
                                            { result ->
                                                mHeroNames = result
                                                ids.forEach { resultNames.add(mHeroNames[it]!!) }
                                                subscriber.onSuccess(resultNames)
                                            },
                                            { error -> subscriber.onError(error) }
                                    )
                        } else {
                            ids.forEach { resultNames.add(mHeroNames[it]!!) }
                            subscriber.onSuccess(resultNames)
                        }
                    }
            )
}