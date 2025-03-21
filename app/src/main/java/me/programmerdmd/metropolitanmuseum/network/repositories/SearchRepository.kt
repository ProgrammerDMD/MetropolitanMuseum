package me.programmerdmd.metropolitanmuseum.network.repositories

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import me.programmerdmd.metropolitanmuseum.network.api.MuseumClient
import me.programmerdmd.metropolitanmuseum.objects.api.MuseumObject

interface SearchRepository {
    suspend fun search(query: String): List<MuseumObject>
}

class SearchRepositoryImpl(
    context: Context
) : SearchRepository {

    private val api = MuseumClient(context).museumAPI

    override suspend fun search(query: String): List<MuseumObject> = withContext(Dispatchers.IO) {
        val response = api.search(query)
        val searchObject = response.body()

        if (!response.isSuccessful || searchObject == null) {
            throw Exception("Search failed!")
        }

        if (searchObject.objectIDs == null) {
            return@withContext emptyList()
        }

        return@withContext searchObject.objectIDs.take(10).map {
            async {
                api.getObject(it)
            }
        }.awaitAll().filter {
            it.isSuccessful && it.body() != null
        }.mapNotNull {
            it.body()
        }
    }

}