package github.com.rhacco.dota2androidapp.sources.repos

import github.com.rhacco.dota2androidapp.api.TopLiveMatchesResponse
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object CustomAPIRepository {
    fun getTopLiveMatches(): Single<List<TopLiveMatchesResponse.Match>> =
            CustomAPIRemoteDataSource.getTopLiveMatches()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}