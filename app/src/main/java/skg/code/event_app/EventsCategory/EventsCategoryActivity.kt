package skg.code.event_app.EventsCategory

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import skg.code.event_app.ApiInterface
import skg.code.event_app.BASE_URL
import skg.code.event_app.EventDataItem
import skg.code.event_app.R

class EventsCategoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EventsCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events_category)

        val category = intent.getStringExtra("CATEGORY") ?: ""
        title = category // Set the category as the screen title


        recyclerView = findViewById(R.id.category_events_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)


        adapter = EventsCategoryAdapter(emptyList())
        recyclerView.adapter = adapter

        fetchCategoryEvents(category)
    }

    private fun fetchCategoryEvents(category: String) {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getEvents()

        retrofitData.enqueue(object : Callback<List<EventDataItem>> {
            override fun onResponse(
                call: Call<List<EventDataItem>>,
                response: Response<List<EventDataItem>>
            ) {
                if (response.isSuccessful) {
                    val events = response.body() ?: emptyList()
                    // Filter events by the selected category
                    val filteredEvents = events.filter { it.event_category == category }
                    adapter.updateEvents(filteredEvents)
                } else {
                    Log.e("CategoryEvents", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<EventDataItem>>, t: Throwable) {
                Log.e("CategoryEvents", "API call failed: ${t.message}")
            }
        })
    }
}