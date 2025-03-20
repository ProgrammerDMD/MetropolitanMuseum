package me.programmerdmd.metropolitanmuseum.network.repositories

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.programmerdmd.metropolitanmuseum.network.api.MuseumClient
import me.programmerdmd.metropolitanmuseum.objects.api.MuseumObject

interface MuseumRepository {
    suspend fun getObject(objectId: Int): MuseumObject?
}

class MuseumRepositoryImpl(context: Context) : MuseumRepository {

    private val api = MuseumClient(context).museumAPI

    override suspend fun getObject(objectId: Int): MuseumObject? = withContext(Dispatchers.IO) {
        val response = api.getObject(objectId)
        val museumObject = response.body()

        if (!response.isSuccessful) {
            throw Exception("Request for getting object failed")
        }

        return@withContext museumObject
    }

}