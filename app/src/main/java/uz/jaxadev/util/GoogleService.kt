package uz.jaxadev.util

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://maps.googleapis.com/"

interface GoogleService {

    @GET("maps/api/directions/json")
    fun getDirectionsAsync(
        @Query("mode") mode: String,
        @Query("transit_routing_preference") transitRoutingPreference: String,
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String
    ): Deferred<Result>

}

val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()


object GoogleApi {
    val googleService: GoogleService by lazy {
        retrofit.create(GoogleService::class.java)
    }
}