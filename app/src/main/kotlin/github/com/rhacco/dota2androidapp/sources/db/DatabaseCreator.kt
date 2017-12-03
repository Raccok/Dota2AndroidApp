package github.com.rhacco.dota2androidapp.sources.db

/**
 * Creates the [AppDatabase] asynchronously, exposing a LiveData object to notify of creation.
 */
// object DatabaseCreator {
//     private val mIsDatabaseCreated = MutableLiveData<Boolean>()
//     lateinit var mDatabase: AppDatabase
//     private val mInitializing = AtomicBoolean(true)
//
//     fun createDb(context: Context) {
//         if (mInitializing.compareAndSet(true, false).not()) {
//             return
//         }
//
//         mIsDatabaseCreated.value = false
//
//         Completable.fromAction {
//             mDatabase = Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME).build()
//         }
//                 .subscribeOn(Schedulers.computation())
//                 .observeOn(AndroidSchedulers.mainThread())
//                 .subscribe({ mIsDatabaseCreated.value = true }, { it.printStackTrace() })
//     }
// }