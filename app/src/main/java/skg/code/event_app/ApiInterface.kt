package skg.code.event_app

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("events")
    fun getEvents(): Call<List<LiveData>>
}