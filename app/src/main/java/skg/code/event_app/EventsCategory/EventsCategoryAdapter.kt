package skg.code.event_app.EventsCategory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import skg.code.event_app.EventDataItem
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
    }

    override fun getItemCount(): Int = eventList.size

    fun updateEvents(events: List<EventDataItem>) {
        eventList = events
        notifyDataSetChanged()
    }

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventTitle: TextView = itemView.findViewById(R.id.event_title)
        val eventDescription: TextView = itemView.findViewById(R.id.event_description)
    }
}