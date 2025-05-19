package skg.code.event_app

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://10.0.2.2:3001/"
class MainActivity : AppCompatActivity() {
    // Utilized Views / Adapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: EventAdapter


    //EventList is a test list for manual insertion of data
    private var eventList = ArrayList<EventDataItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        searchView = findViewById(R.id.searchView)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // This function is inserting data to the eventList manually
        //addEventToList()

        // MongoDb data
        adapter = EventAdapter(emptyList())
        recyclerView.adapter = adapter

        fetchEventCategory()
    }
    //Manual insertion of data to the eventList Object
    private fun addEventToList(){
        eventList.add(
            EventDataItem(
                event_booked = false,
                event_category = "Concert",
                event_date = "2023-10-01T00:00:00Z",
                event_description = "This is a description of the event",
                event_location = "Thessaloniki",
                event_organizer = "John Doe",
                event_price = 20.0,
                event_time = "18:00",
                event_title = "Concert Title",
                venue = "Venue Name",
                __v = 0,
                _id = "123",
            ))
    }


    private fun fetchEventCategory() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getEvents()
        Log.d("testing", "getMyData: $retrofitData")

        retrofitData.enqueue(object : Callback<List<EventDataItem>> {
            override fun onResponse(
                call: Call<List<EventDataItem>>,
                response: Response<List<EventDataItem>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("MainActivity", "Events received: ${responseBody?.size}")

                    if (responseBody != null) {
                        // Update RecyclerView with data from MongoDB
                        adapter = EventAdapter(responseBody)
                        recyclerView.adapter = adapter
                    }
                } else {
                    Log.e("MainActivity", "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<EventDataItem>>, t: Throwable) {
                Log.e("MainActivity", "API call failed: ${t.message}")
            }
        })
    }




}