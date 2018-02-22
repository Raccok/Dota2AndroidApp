package github.com.rhacco.dota2androidapp.sources.databases.daos

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import github.com.rhacco.dota2androidapp.sources.databases.entities.ItemEntity
import io.reactivex.Single

@Dao
interface ItemsDao {
    @Query("SELECT * FROM items")
    fun getItems(): Single<List<ItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun storeItems(items: List<ItemEntity>)
}