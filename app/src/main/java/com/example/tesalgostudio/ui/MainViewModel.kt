package com.example.tesalgostudio.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tesalgostudio.data.ApiConfig
import com.example.tesalgostudio.ApiResponse
import com.example.tesalgostudio.MemesItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel(){
    
    companion object{
        private const val TAG = "MainViewModel"
    }
    
    private var _memesResult = MutableLiveData<List<MemesItem>>()
    private var _loading = MutableLiveData(false)
    val memesResult get() =  _memesResult
    val loading get() = _loading


    init {
        getMemesFromApi()
    }

    fun getMemesFromApi(){
        _loading.value = true
        val client = ApiConfig.getApiService().getMemes()
        client.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(
                call: Call<ApiResponse>,
                response: Response<ApiResponse>
            ) {
                if(response.isSuccessful){
                    _memesResult.value = response.body()?.data?.memes
                    _loading.value = false
                }else{
                    Log.e(TAG, "responseFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}