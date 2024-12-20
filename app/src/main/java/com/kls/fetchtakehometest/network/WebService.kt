package com.kls.fetchtakehometest.network

import com.kls.fetchtakehometest.data.Data
import retrofit2.http.GET

interface WebService {
    companion object {
        const val BASE_URL = "https://fetch-hiring.s3.amazonaws.com/"
    }

    @GET("hiring.json")
    suspend fun getHiringItems(): List<Data>
}