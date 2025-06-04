package skg.code.event_app

import java.util.Date

data class EventDataItem(
    val __v: Int,
    val _id: String,
    val event_booked: Boolean,
    val event_category: String,
    val event_date: Date,
    val event_description: String,
    val event_location: String,
    val event_organizer: String,
    val event_price: Double,
    val event_time: String,
    val event_title: String,
    val venue: String,
    val category_image_url: String = "",
    val event_image_url: String = "",
    val trending: Boolean
)