package me.programmerdmd.metropolitanmuseum.objects.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoriteObject(
    @PrimaryKey val id: Int
)