package me.programmerdmd.metropolitanmuseum.network.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import me.programmerdmd.metropolitanmuseum.objects.api.MuseumObject
import me.programmerdmd.metropolitanmuseum.objects.api.SearchObject
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

private fun hasNetwork(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork ?: return false
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
    return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

class MuseumClient(
    context: Context
) {
    private val cacheSize = (100 * 1024 * 1024).toLong()
    private val myCache = Cache(context.cacheDir, cacheSize)
    private val okHttpClient = OkHttpClient.Builder()
        .cache(myCache)
        .addNetworkInterceptor(
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .addInterceptor { chain ->
            var request = chain.request()
            if (!hasNetwork(context)) {
                val cacheControl = CacheControl.Builder()
                    .onlyIfCached()
                    .maxStale(7, TimeUnit.DAYS)
                    .build()

                request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build()
            } else {
                val cacheControl = CacheControl.Builder()
                    .maxAge(30, TimeUnit.MINUTES)
                    .maxStale(7, TimeUnit.DAYS)
                    .build()

                request = request.newBuilder()
                    .header("Cache-Control", cacheControl.toString())
                    .build()
            }
            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://collectionapi.metmuseum.org/public/collection/v1/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val museumAPI = retrofit.create(MuseumAPI::class.java)
}

interface MuseumAPI {
    @GET("objects")
    suspend fun getObjects(): Response<SearchObject>

    @GET("search")
    suspend fun search(
        @Query("q") query: String
    ): Response<SearchObject>

    @GET("objects/{id}")
    suspend fun getObject(
        @Path("id") id: Int
    ): Response<MuseumObject?>


}