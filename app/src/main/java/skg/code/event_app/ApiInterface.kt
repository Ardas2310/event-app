package skg.code.event_app

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    @GET("events") // Make sure this endpoint is correct
    fun getEvents(): Call<List<EventDataItem>>
}