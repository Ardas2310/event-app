package skg.code.event_app.EventsCategory

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

class EventsCategoryAdapter(private var eventList: List<EventDataItem>) :
    RecyclerView.Adapter<EventsCategoryAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_category_items, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]

        holder.eventTitle.text = event.event_title
        holder.eventDescription.text = event.event_description

        holder.eventPrice.text = "${event.event_price}€"

        holder.eventImagePreview.load(event.event_image_url){
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_background)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, EventDetailsActivity::class.java)
            intent.putExtra("EVENT_ID", event._id)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = eventList.size

    fun updateEvents(events: List<EventDataItem>) {
        eventList = events
        notifyDataSetChanged()
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventTitle: TextView = itemView.findViewById(R.id.event_title)
        val eventDescription: TextView = itemView.findViewById(R.id.event_description)
        val eventPrice: TextView = itemView.findViewById(R.id.event_price)
        val eventImagePreview: ImageView = itemView.findViewById(R.id.event_image_result)
    }
}