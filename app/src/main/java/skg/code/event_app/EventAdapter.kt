package skg.code.event_app

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import skg.code.event_app.EventsCategory.EventsCategoryActivity

class EventAdapter(var eventList: List<EventDataItem>): RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    private var fullEventList: List<EventDataItem> = eventList
    private var uniqueCategories: List<String> = eventList.map { it.event_category }.distinct()

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
        val category = uniqueCategories[position]
        holder.event_category.text = category

        // How we make each item clickable
        holder.itemView.setOnClickListener {
            val context = holder.event_category.context
            val intent = Intent(context, EventsCategoryActivity::class.java).apply {
                putExtra("CATEGORY", category)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return uniqueCategories.size
    }

    fun updateEvents(newEvents: List<EventDataItem>) {
        fullEventList = newEvents
        uniqueCategories = fullEventList.map { it.event_category }.distinct()
        notifyDataSetChanged()
    }


    inner class EventViewHolder(eventView : View): RecyclerView.ViewHolder(eventView){
        val event_category : TextView = eventView.findViewById(R.id.text_event_type)
//        val event_description : TextView = eventView.findViewById(R.id.tvEventDescription)
    }
}