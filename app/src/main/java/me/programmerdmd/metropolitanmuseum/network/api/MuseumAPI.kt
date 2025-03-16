package me.programmerdmd.metropolitanmuseum.network.api

import me.programmerdmd.metropolitanmuseum.objects.api.MuseumObject
import me.programmerdmd.metropolitanmuseum.objects.api.SearchObject
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private val okHttpClient = OkHttpClient.Builder()
    .addNetworkInterceptor(
        HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    )
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl("https://collectionapi.metmuseum.org/public/collection/v1/")
    .client(okHttpClient)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

val museumAPI = retrofit.create(MuseumAPI::class.java)

interface MuseumAPI {

    @GET("search")
    suspend fun search(
        @Query("q") query: String
    ): Response<SearchObject>

    @GET("objects/{id}")
    suspend fun getObject(
        @Path("id") id: Int
    ): Response<MuseumObject>


}