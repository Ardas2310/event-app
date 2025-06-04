package skg.code.event_app

data class TrendingEvent(
    val id: String = "",
    val title: String = "",
    val imageUrl: String = "",
    val date: String = "",
    val location: String = "",
    val attendeeCount: Int = 0
)