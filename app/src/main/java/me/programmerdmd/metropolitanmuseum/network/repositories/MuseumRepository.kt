package me.programmerdmd.metropolitanmuseum.network.repositories

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import me.programmerdmd.metropolitanmuseum.network.api.MuseumClient
import me.programmerdmd.metropolitanmuseum.objects.api.MuseumObject

interface MuseumRepository {
    suspend fun getObjects(page: Int): List<MuseumObject>
    suspend fun getObject(objectId: Int): MuseumObject?
    suspend fun search(query: String, page: Int): List<MuseumObject>
}

class MuseumRepositoryImpl(context: Context) : MuseumRepository {

    private val api = MuseumClient(context).museumAPI
    private val pageSize = 20

    override suspend fun getObjects(page: Int): List<MuseumObject> = withContext(Dispatchers.IO) {
        val response = api.getObjects()
        val searchObject = response.body()

        if (!response.isSuccessful || searchObject == null) {
            throw Exception("Search failed!")
        }

        if (searchObject.objectIDs == null) {
            return@withContext emptyList()
        }

        val objectIds = searchObject.objectIDs
        val startIndex = page * pageSize
        val endIndex = minOf(startIndex + pageSize, objectIds.size)
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
        }.awaitAll().filterNotNull()

        return@withContext museumObjects
    }

    override suspend fun getObject(objectId: Int): MuseumObject? = withContext(Dispatchers.IO) {
        val response = api.getObject(objectId)
        val museumObject = response.body()

        if (!response.isSuccessful) {
            throw Exception("Request for getting object failed")
        }

        return@withContext museumObject
    }

    override suspend fun search(query: String, page: Int): List<MuseumObject> = withContext(Dispatchers.IO) {
        val response = api.search(query)
        val searchObject = response.body()

        if (!response.isSuccessful || searchObject == null) {
            throw Exception("Search failed!")
        }

        if (searchObject.objectIDs == null) {
            return@withContext emptyList()
        }

        val objectIds = searchObject.objectIDs
        val startIndex = page * pageSize
        val endIndex = minOf(startIndex + pageSize, objectIds.size)
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
        }.awaitAll().filterNotNull()

        return@withContext museumObjects
    }

}