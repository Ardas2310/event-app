package skg.code.event_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.RecyclerView
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import skg.code.event_app.Search.SearchActivity
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://event-app-backend-evev.onrender.com/"
class MainActivity : AppCompatActivity() {
    // Utilized Views / Adapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EventAdapter
    private lateinit var drawerLayout: DrawerLayout


    //EventList is a test list for manual insertion of data
    private var eventList = ArrayList<EventDataItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Thread.sleep(3000)
        installSplashScreen()


        setContentView(R.layout.activity_main)

        // Circular Progress Bar for main recyclerview.
        var progressBarRV = findViewById<ProgressBar>(R.id.progress_circular_rv)
        recyclerView = findViewById(R.id.MainRecyclerView)






        recyclerView.layoutManager = GridLayoutManager(this, 2)
        findViewById<androidx.cardview.widget.CardView>(R.id.search_button).setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }


        // DrawerLayout for the navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout)
        val userImageButton = findViewById<ImageView>(R.id.user_profile_image)
        userImageButton.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END)
            } else {
                drawerLayout.openDrawer(GravityCompat.END)
            }
        }


        // This function is inserting data to the eventList manually
        //addEventToList()

        // MongoDb data
        adapter = EventAdapter(emptyList())
        recyclerView.adapter = adapter

        fetchEventCategory(progressBarRV)
    }


    // DO NOT remove this function, it is used for testing purposes.
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


    private fun fetchEventCategory(progressBarRV: ProgressBar? = null) {
        showProgressBar(progressBarRV)

        // Set up OkHttpClient with timeouts.
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
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
                        hideProgressBar(progressBarRV)
                    }
                } else {
                    Log.e("MainActivity", "Error: ${response.code()} - ${response.message()}")
                    hideProgressBar(progressBarRV)
                }
            }

            override fun onFailure(call: Call<List<EventDataItem>>, t: Throwable) {
                Log.e("MainActivity", "API call failed: ${t.message}")
                hideProgressBar(progressBarRV)
            }
        })
    }

    private fun showProgressBar(progressBar: ProgressBar?) {
        progressBar?.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    private fun hideProgressBar(progressBar: ProgressBar?) {
        progressBar?.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }




}