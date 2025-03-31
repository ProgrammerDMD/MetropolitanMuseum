package me.programmerdmd.metropolitanmuseum.objects.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favoriteobject")
    fun getAll(): List<FavoriteObject>

    @Query("SELECT * FROM favoriteobject")
    fun getAllFlow(): Flow<List<FavoriteObject>>

    @Query("SELECT EXISTS(SELECT 1 FROM favoriteobject WHERE id = :id)")
    fun exists(id: Int): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg objects: FavoriteObject)

    @Delete
    fun delete(favoriteObject: FavoriteObject)
}