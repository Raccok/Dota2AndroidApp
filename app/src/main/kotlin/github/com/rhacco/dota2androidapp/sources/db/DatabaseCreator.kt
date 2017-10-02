package github.com.rhacco.dota2androidapp.sources.db

import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.room.Room
import android.content.Context
import github.com.rhacco.dota2androidapp.sources.db.AppDatabase.Companion.DATABASE_NAME
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Creates the [AppDatabase] asynchronously, exposing a LiveData object to notify of creation.
 */
object DatabaseCreator {
    private val mIsDatabaseCreated = MutableLiveData<Boolean>()
    lateinit var mDatabase: AppDatabase
    private val mInitializing = AtomicBoolean(true)

    fun createDb(context: Context) {
        if (mInitializing.compareAndSet(true, false).not()) {
            return
        }

        mIsDatabaseCreated.value = false

        Completable.fromAction {
            mDatabase = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
        }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ mIsDatabaseCreated.value = true }, { it.printStackTrace() })
    }
}
