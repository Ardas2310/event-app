package skg.code.event_app

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {
    @GET("events") // Make sure this endpoint is correct
    fun getEvents(): Call<List<EventDataItem>>

    @GET("events/{id}")
    fun getEventsById(@Path("id") id: String): Call<EventDataItem>
}