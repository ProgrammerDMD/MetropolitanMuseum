package me.programmerdmd.metropolitanmuseum.network.repositories

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import me.programmerdmd.metropolitanmuseum.network.api.MuseumClient
import me.programmerdmd.metropolitanmuseum.objects.api.MuseumObject

interface MuseumRepository {
    suspend fun getObjects(page: Int): Flow<List<MuseumObject>>
    suspend fun getObject(objectId: Int): MuseumObject?
}

class MuseumRepositoryImpl(context: Context) : MuseumRepository {

    private val api = MuseumClient(context).museumAPI
    private val pageSize = 20

    override suspend fun getObjects(page: Int): Flow<List<MuseumObject>> = flow<List<MuseumObject>> {
        val response = api.getObjects()
        val searchObject = response.body()

        if (!response.isSuccessful || searchObject == null) {
            throw Exception("Search failed!")
        }

        if (searchObject.objectIDs == null) {
            emit(emptyList())
            return@flow
        }

        val objectIds = searchObject.objectIDs
        val startIndex = page * pageSize
        val endIndex = minOf(startIndex + pageSize, objectIds.size)
        val idsToFetch = objectIds.subList(startIndex, endIndex)

        val museumObjects = withContext(Dispatchers.IO) {
            idsToFetch.map { objectId ->
                async {
                    try {
                        val objectResponse = api.getObject(objectId)
                        objectResponse.body()
                    } catch (e: Exception) {
                        null
                    }
                }
            }.awaitAll().filterNotNull()
        }

        emit(museumObjects)
    }.flowOn(Dispatchers.IO)

    override suspend fun getObject(objectId: Int): MuseumObject? = withContext(Dispatchers.IO) {
        val response = api.getObject(objectId)
        val museumObject = response.body()

        if (!response.isSuccessful) {
            throw Exception("Request for getting object failed")
        }

        return@withContext museumObject
    }

}