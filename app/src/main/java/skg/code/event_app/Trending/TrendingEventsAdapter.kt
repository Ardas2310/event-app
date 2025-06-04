package skg.code.event_app.Trending

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import skg.code.event_app.EventDataItem
import skg.code.event_app.EventDetails.EventDetailsActivity
import skg.code.event_app.R

class TrendingEventsAdapter(private var events: List<EventDataItem>):
    RecyclerView.Adapter<TrendingEventsAdapter.TrendingEventViewHolder>() {

    private var onEventClickListener: ((EventDataItem) -> Unit)? = null
    fun setOnEventClickListener(listener:(EventDataItem) -> Unit){

        onEventClickListener = listener

    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrendingEventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_trending_event, parent, false)
        return TrendingEventViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TrendingEventViewHolder,
        position: Int
    ) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int {
        return events.size
    }

    fun updateEvents(newEvents: List<EventDataItem>){
        events = newEvents
        notifyDataSetChanged()
    }


    inner class TrendingEventViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        private val EventImage: ImageView = itemView.findViewById(R.id.EventImage)
        private val EventTitle: TextView = itemView.findViewById(R.id.EventTitle)
        private val EventDate: TextView = itemView.findViewById(R.id.EventDate)

        fun bind(event: EventDataItem){
            EventTitle.text = event.event_title
            EventDate.text = "${event.event_date} • ${event.event_location}"
            EventImage.load(event.event_image_url) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_background)
            }

            itemView.setOnClickListener {

                val intent = Intent(itemView.context, EventDetailsActivity::class.java)
                intent.putExtra("EVENT_ID", event._id)
                itemView.context.startActivity(intent)
            }

        }
    }
}