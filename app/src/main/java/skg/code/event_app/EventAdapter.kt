package skg.code.event_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EventAdapter(var eventList: List<EventDataItem>): RecyclerView.Adapter<EventAdapter.EventViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_items, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: EventViewHolder,
        position: Int
    ) {
        holder.event_description.text = eventList[position].event_description // Change it to an image
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    inner class EventViewHolder(eventView : View): RecyclerView.ViewHolder(eventView){
//        val event_title : TextView = eventView.findViewById(R.id.tvEventTitle)
        val event_description : TextView = eventView.findViewById(R.id.tvEventDescription)
    }
}