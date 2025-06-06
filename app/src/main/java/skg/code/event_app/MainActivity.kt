package skg.code.event_app

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import skg.code.event_app.Search.SearchActivity
import skg.code.event_app.Trending.TrendingEventsAdapter
import skg.code.event_app.Trending.addCarouselEffect
import skg.code.event_app.util.getSpecificEventDate
import java.util.Calendar
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://event-app-backend-evev.onrender.com/"
class MainActivity : AppCompatActivity() {
    // Utilized Views / Adapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EventAdapter
    private lateinit var drawerLayout: DrawerLayout

    //For Carousel
    private lateinit var trendingEventsCarousel: ViewPager2
    private lateinit var carouselIndicators: LinearLayout
    private lateinit var trendingEventsAdapter: TrendingEventsAdapter
    private val trendingEvents = mutableListOf<EventDataItem>()
    private val autoScrollHandler = Handler(Looper.getMainLooper())
    private var autoScrollRunnable: Runnable? = null


    //EventList is a test list for manual insertion of data
    private var eventList = ArrayList<EventDataItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Splash Screen loading time
        Thread.sleep(3000)
        installSplashScreen()

        // Content View MainActivity
        setContentView(R.layout.activity_main)

        // RecyclerView
        recyclerView = findViewById(R.id.MainRecyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)


        // Initialize ViewPager2
        trendingEventsCarousel = findViewById(R.id.trendingEventsCarousel)
        carouselIndicators = findViewById(R.id.carouselIndicators)

        setupTrendingEventsCarousel()


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

        fetchEventCategory()
    }


    // DO NOT remove this function, it is used for testing purposes.
    //Manual insertion of data to the eventList Object
    private fun addEventToList(){
        eventList.add(
            EventDataItem(
                event_booked = false,
                event_category = "Concert",
                event_date = getSpecificEventDate(2023, Calendar.OCTOBER, 1, 0, 0),
                event_description = "This is a description of the event",
                event_location = "Thessaloniki",
                event_organizer = "John Doe",
                event_price = 20.0,
                event_time = "18:00",
                event_title = "Concert Title",
                venue = "Venue Name",
                __v = 0,
                _id = "123",
                trending = true,
            ))
    }

    private fun setupTrendingEventsCarousel() {
        trendingEventsAdapter = TrendingEventsAdapter(trendingEvents)

        trendingEventsCarousel.adapter = trendingEventsAdapter
        trendingEventsCarousel.addCarouselEffect(enableZoom = true)

        // Setup page change callback for indicators
        trendingEventsCarousel.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            private var isUserScrolling = false
            private var isUserInteracting = false

            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                when (state) {
                    ViewPager2.SCROLL_STATE_DRAGGING -> {
                        // User started dragging
                        isUserScrolling = true
                        isUserInteracting = true
                        stopAutoScroll()
                    }
                    ViewPager2.SCROLL_STATE_SETTLING -> {
                        // Page is settling into final position
                        // Don't restart auto-scroll yet
                    }
                    ViewPager2.SCROLL_STATE_IDLE -> {
                        // Page has settled
                        if (isUserScrolling) {
                            isUserScrolling = false
                            autoScrollHandler.postDelayed({
                                if(!isUserInteracting){
                                    startAutoScroll()
                                }
                            },4000)
                        }
                    }
                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicators(position)
            }
        })
    }


    private fun fetchEventCategory() {
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

                        // Update trending events with the fetched data
                        updateTrendingEvents(responseBody)
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

    private fun updateTrendingEvents(allEvents: List<EventDataItem>) {
        // Filter trending events or get top 5 events
        val filteredTrendingEvents = allEvents.filter { it.trending }.take(5)

        // If no trending events, take first 5 events
        val eventsToShow = if (filteredTrendingEvents.isNotEmpty()) {
            filteredTrendingEvents
        } else {
            allEvents.take(5)
        }

        trendingEvents.clear()
        trendingEvents.addAll(eventsToShow)
        trendingEventsAdapter.updateEvents(trendingEvents)

        if (trendingEvents.isNotEmpty()) {
            setupIndicators(trendingEvents.size)
            startAutoScroll()
        }
    }


    private fun setupIndicators(count: Int) {
        carouselIndicators.removeAllViews()

        for (i in 0 until count) {
            val indicator = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(16, 16).apply {
                    setMargins(4, 0, 4, 0)
                }
                background = ContextCompat.getDrawable(
                    this@MainActivity,
                    if (i == 0) R.drawable.indicator_active else R.drawable.indicator_inactive
                )
            }
            carouselIndicators.addView(indicator)
        }
    }

    private fun updateIndicators(position: Int) {
        for (i in 0 until carouselIndicators.childCount) {
            val indicator = carouselIndicators.getChildAt(i)
            indicator.background = ContextCompat.getDrawable(
                this,
                if (i == position) R.drawable.indicator_active else R.drawable.indicator_inactive
            )
        }
    }

    private fun startAutoScroll() {
        stopAutoScroll() // Always stop existing auto-scroll first

        if (trendingEvents.size <= 1) return // Don't auto-scroll if only one item

        autoScrollRunnable = object : Runnable {
            override fun run() {
                if (trendingEvents.isNotEmpty() && trendingEventsCarousel.isAttachedToWindow) {
                    val currentItem = trendingEventsCarousel.currentItem
                    val nextItem = (currentItem + 1) % trendingEvents.size
                    trendingEventsCarousel.setCurrentItem(nextItem, true)
                    autoScrollHandler.postDelayed(this, 8000) // 10 seconds
                }
            }
        }
        autoScrollHandler.postDelayed(autoScrollRunnable!!, 8000)
    }

    private fun stopAutoScroll() {
        autoScrollRunnable?.let {
            autoScrollHandler.removeCallbacks(it)
            autoScrollRunnable = null
        }
    }

    private fun resetAutoScroll() {
        stopAutoScroll()
        // Add a small delay before restarting to avoid conflicts
        autoScrollHandler.postDelayed({
            startAutoScroll()
        }, 1000) // 1 second delay
    }

    override fun onResume() {
        super.onResume()
        if (trendingEvents.isNotEmpty()) {
            startAutoScroll()
        }
    }

    override fun onPause() {
        super.onPause()
        stopAutoScroll()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAutoScroll()
    }

}
