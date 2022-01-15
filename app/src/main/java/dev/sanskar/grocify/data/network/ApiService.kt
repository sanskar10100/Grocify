package dev.sanskar.grocify.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.sanskar.grocify.data.model.ApiResponse
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

const val BASE_URL = "https://api.data.gov.in/resource/"

val retrofit: Retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(
            Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        ))
        .build()

interface PriceApi {
    // The limit is arbitrary at this point because omitting it results in only 10 results being returned
    // Actual count may be much higher than the limit.
    @GET("9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001b8fc29fa3b09486f4d9b26841e3c8941b&format=json&offset=0&limit=1000")
    suspend fun getRecords(): ApiResponse
}

object ApiService {
    val retrofitService: PriceApi by lazy {
        retrofit.create(PriceApi::class.java)
    }
}