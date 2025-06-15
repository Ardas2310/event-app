package skg.code.event_app.Search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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

class SearchActivity : AppCompatActivity() {

    private lateinit var searchView: RecyclerView
    private lateinit var searchText: EditText
    private lateinit var adapter: SearchAdapter
    private var allEvents = listOf<EventDataItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchText = findViewById(R.id.search_edit_text)
        searchView = findViewById(R.id.search_results_recycler_view)
        findViewById<ImageView>(R.id.back_button).setOnClickListener { finish() }

        adapter = SearchAdapter(emptyList())

        searchView.layoutManager = LinearLayoutManager(this)
        searchView.adapter = adapter


        fetchEvents()

        searchText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {

            }

            override fun onTextChanged(
                p0: CharSequence?,
                p1: Int,
                p2: Int,
                p3: Int
            ) {
                searchEvents(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })



    }


    private fun fetchEvents() {
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
                    val responseBody = response.body()
                    if (responseBody != null) {
                        allEvents = responseBody
                        adapter.updateResults(allEvents.take(5))
                    }
                }
            }

            override fun onFailure(call: Call<List<EventDataItem>?>, t: Throwable) {
                Log.e("SearchActivity", "API call failed: ${t.message}")
                Toast.makeText(applicationContext, "Sorry! Something went wrong.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun searchEvents(query: String) {
        if(query.isEmpty()) {
            adapter.updateResults(allEvents.take(5))
            findViewById<TextView>(R.id.suggestions_title).text = "Suggestions"
        } else {
            val filteredEvents = allEvents.filter {
                it.event_title.contains(query, ignoreCase = true) ||
                it.event_description.contains(query, ignoreCase = true) ||
                it.event_location.contains(query, ignoreCase = true) ||
                it.event_category.contains(query, ignoreCase = true)
            }
            adapter.updateResults(filteredEvents)
            findViewById<TextView>(R.id.suggestions_title).text = "Results"
        }
    }


}