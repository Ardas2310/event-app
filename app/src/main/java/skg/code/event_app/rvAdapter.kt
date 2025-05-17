package skg.code.event_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LiveDataAdapter(private var events: List<LiveData>) :
    RecyclerView.Adapter<LiveDataAdapter.LiveDataViewHolder>() {

    inner class LiveDataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvEventDescription: TextView = itemView.findViewById(R.id.tvEventDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LiveDataViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return LiveDataViewHolder(view)
    }

    override fun onBindViewHolder(holder: LiveDataViewHolder, position: Int) {
        val event = events[position]
        holder.tvEventDescription.text = event.event_description // Show only description
    }

    override fun getItemCount(): Int = events.size

    fun updateEvents(newEvents: List<LiveData>) {
        events = newEvents
        notifyDataSetChanged()
    }
}
