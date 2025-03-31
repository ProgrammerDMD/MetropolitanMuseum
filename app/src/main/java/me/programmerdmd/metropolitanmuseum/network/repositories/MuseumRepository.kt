package me.programmerdmd.metropolitanmuseum.network.repositories

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import me.programmerdmd.metropolitanmuseum.network.api.MuseumClient
import me.programmerdmd.metropolitanmuseum.objects.api.MuseumObject
import me.programmerdmd.metropolitanmuseum.objects.database.FavoriteDao

interface MuseumRepository {
    suspend fun getObjects(page: Int): List<MuseumObject?>
    suspend fun getObjectsFromLocal(page: Int): List<MuseumObject?>
    suspend fun getObject(objectId: Int): MuseumObject?
    suspend fun search(query: String, page: Int): List<MuseumObject?>
}

class MuseumRepositoryImpl(
    context: Context,
    private val favoriteDao: FavoriteDao
) : MuseumRepository {

    private val api = MuseumClient(context).museumAPI
    private val pageSize = 20

    override suspend fun getObjects(page: Int): List<MuseumObject?> = withContext(Dispatchers.IO) {
        val response = api.getObjects()
        val searchObject = response.body()

        if (!response.isSuccessful || searchObject == null || searchObject.objectIDs == null) {
            return@withContext emptyList()
        }

        val objectIds = searchObject.objectIDs
        val startIndex = page * pageSize
        val endIndex = minOf(startIndex + pageSize, objectIds.size)

        if (startIndex > endIndex) {
            return@withContext emptyList()
        }

        val idsToFetch = objectIds.subList(startIndex, endIndex)

        val museumObjects = idsToFetch.map { objectId ->
            async {
                try {
                    val objectResponse = api.getObject(objectId)
                    objectResponse.body()
                } catch (e: Exception) {
                    null
                }
            }
        }.awaitAll()

        return@withContext museumObjects
    }

    // Just a simple copy paste to not overcomplicate stuff
    override suspend fun getObjectsFromLocal(page: Int): List<MuseumObject?> = withContext(Dispatchers.IO) {
        val favoriteObjects = favoriteDao.getAll()
        val startIndex = page * pageSize
        val endIndex = minOf(startIndex + pageSize, favoriteObjects.size)

        if (startIndex > endIndex) {
            return@withContext emptyList()
        }

        val idsToFetch = favoriteObjects.subList(startIndex, endIndex)

        val museumObjects = idsToFetch.map { favoriteObject ->
            async {
                try {
                    val objectResponse = api.getObject(favoriteObject.id)
                    objectResponse.body()
                } catch (e: Exception) {
                    null
                }
            }
        }.awaitAll()

        return@withContext museumObjects
    }

    override suspend fun getObject(objectId: Int): MuseumObject? = withContext(Dispatchers.IO) {
        val response = api.getObject(objectId)
        val museumObject = response.body()

        if (!response.isSuccessful) {
           return@withContext null
        }

        return@withContext museumObject
    }

    override suspend fun search(query: String, page: Int): List<MuseumObject?> = withContext(Dispatchers.IO) {
        val response = api.search(query)
        val searchObject = response.body()

        if (!response.isSuccessful || searchObject == null || searchObject.objectIDs == null) {
            return@withContext emptyList()
        }

        val objectIds = searchObject.objectIDs
        val startIndex = page * pageSize
        val endIndex = minOf(startIndex + pageSize, objectIds.size)

        if (startIndex > endIndex) {
            return@withContext emptyList()
        }

        val idsToFetch = objectIds.subList(startIndex, endIndex)

        val museumObjects = idsToFetch.map { objectId ->
            async {
                try {
                    val objectResponse = api.getObject(objectId)
                    objectResponse.body()
                } catch (e: Exception) {
                    null
                }
            }
        }.awaitAll()

        return@withContext museumObjects
    }

}