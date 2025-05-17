package skg.code.event_app

data class LiveData(
    val event_booked: Boolean,
    val event_category: String,
    val event_date: String,
    val event_description: String,
    val event_location: String,
    val event_organizer: String,
    val event_price: Double,
    val event_time: String,
    val event_title: String,
    val venue: String
)