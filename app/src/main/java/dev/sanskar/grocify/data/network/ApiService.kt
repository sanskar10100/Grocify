package dev.sanskar.grocify.data.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.sanskar.grocify.data.model.ApiResponse
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

val BASE_URL = "https://api.data.gov.in/resource/"

val retrofit =
    Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(
            Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        ))
        .build()

interface PriceApi {
    @GET("9ef84268-d588-465a-a308-a864a43d0070?api-key=579b464db66ec23bdd000001b8fc29fa3b09486f4d9b26841e3c8941b&format=json&offset=0&limit=2000")
    suspend fun getRecords(): ApiResponse
}

object ApiService {
    val retrofitService by lazy {
        retrofit.create(PriceApi::class.java)
    }
}