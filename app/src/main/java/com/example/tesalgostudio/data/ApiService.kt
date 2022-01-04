package com.example.tesalgostudio.data

import com.example.tesalgostudio.ApiResponse
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("get_memes")
   fun getMemes() : Call<ApiResponse>
}