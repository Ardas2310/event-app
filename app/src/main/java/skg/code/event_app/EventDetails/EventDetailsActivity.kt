package skg.code.event_app.EventDetails

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import skg.code.event_app.ApiInterface
import skg.code.event_app.BASE_URL
import skg.code.event_app.EventDataItem
import skg.code.event_app.R
import skg.code.event_app.ui.drawable.createShimmerDrawable
import skg.code.event_app.util.formatDate

class EventDetailsActivity: AppCompatActivity() {

    private lateinit var backButton : ImageView
    private lateinit var eventImageView: ImageView
    private lateinit var eventTitleTextView: TextView
    private lateinit var eventDateTextView: TextView
    private lateinit var eventLocationTextView: TextView
    private lateinit var eventDescriptionTextView: TextView
    private lateinit var buyTicketsButton: ExtendedFloatingActionButton






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detailed_event)

        // Initialize views
        backButton = findViewById(R.id.back_button)
        eventImageView = findViewById(R.id.event_image_detail)
        eventTitleTextView = findViewById(R.id.detailed_event_title)
        eventDateTextView = findViewById(R.id.detailed_event_date)
        eventLocationTextView = findViewById(R.id.detailed_event_location)
        eventDescriptionTextView = findViewById(R.id.detailed_event_description)
        buyTicketsButton = findViewById(R.id.buy_tickets_button)

        val eventId = intent.getStringExtra("EVENT_ID") ?: throw IllegalArgumentException("Event ID is required")

        // Set up the back button to finish the activity when clicked
        backButton.setOnClickListener { finish() }

        // Fetch event details using the provided event ID
        fetchEventDetails(eventId)

        //
        buyTicketsButton.setOnClickListener { Snackbar.make(it,"Booking tickets for events...",
            Snackbar.LENGTH_LONG).show() }
    }

    private fun fetchEventDetails(eventId: String) {

        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)



        retrofitBuilder.getEventsById(eventId).enqueue(object : Callback<EventDataItem> {
            override fun onResponse(call: Call<EventDataItem>, response: Response<EventDataItem>) {
                if (response.isSuccessful) {
                    val event = response.body()
                    if (event != null) {
                        updateUI(event)
                    }
                } else {
                    showError("Failed to load event details")
                }
            }

            override fun onFailure(call: Call<EventDataItem>, t: Throwable) {
                showError("Network error: ${t.message}")
            }
        })
    }


    private fun showError(message: String) {
        Log.e("EventDetailsActivity", message)
        //TODO: Johnny vale ena Toast pali opws to allo otan to deis
    }


    private fun updateUI(event: EventDataItem) {
        eventTitleTextView.text = event.event_title
        eventDateTextView.text = "${formatDate(event.event_date)} at ${event.event_time}"
        eventLocationTextView.text ="${event.event_location}, ${event.venue}"
        eventDescriptionTextView.text = event.event_description

        val shimmerDrawable = createShimmerDrawable()

        eventImageView.load(event.event_image_url) {
            crossfade(true)
            placeholder(shimmerDrawable)
            error(R.drawable.ic_launcher_background)
        }

        buyTicketsButton.text = "Tickets from ${event.event_price}€"

    }

}


