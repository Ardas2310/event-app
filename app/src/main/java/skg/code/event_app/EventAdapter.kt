package skg.code.event_app

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import skg.code.event_app.EventsCategory.EventsCategoryActivity

class EventAdapter(var eventList: List<EventDataItem>): RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    private var fullEventList: List<EventDataItem> = eventList
    private var uniqueCategories: List<String> = eventList.map { it.event_category }.distinct()
    private var categoryImageMap: Map<String, String> = createCategoryImageMap()


    // function to create a mpa of category and image
    private fun createCategoryImageMap(): Map<String, String> {
        val categoryMap = mutableMapOf<String, String>()

        for (event in fullEventList){
            if(!categoryMap.containsKey(event.event_category)){
                categoryMap[event.event_category] = event.category_image_url
            }
        }

        return categoryMap
    }

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

        val imageUrl = categoryImageMap[category] ?: ""
        if(imageUrl.isNotEmpty()) {
            holder.imageView.load(imageUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_background)
                error(R.drawable.ic_launcher_background)
            }
        } else {
            holder.imageView.load(R.drawable.ic_launcher_background)
        }


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
        categoryImageMap = createCategoryImageMap()
        notifyDataSetChanged()
    }


    inner class EventViewHolder(eventView : View): RecyclerView.ViewHolder(eventView){
        val event_category : TextView = eventView.findViewById(R.id.text_event_type)
        val imageView : ImageView = eventView.findViewById(R.id.event_image_view)
    }
}