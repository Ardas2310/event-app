package skg.code.event_app.Search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import skg.code.event_app.EventDataItem
import skg.code.event_app.R

class SearchAdapter(var results: List<EventDataItem>): RecyclerView.Adapter<SearchAdapter.ResultViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder{
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.items_search_result, parent, false)
        return ResultViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ResultViewHolder,
        position: Int
    ) {

        val event = results[position]
        // Title
        holder.event_title.text = event.event_title
        // Category
        holder.event_category.text = "${event.event_category} - ${event.event_location}"
        // Image
        holder.event_Image.load(event.event_image_url){
            crossfade(true)
            placeholder(R.drawable.ic_launcher_background)
            error(R.drawable.ic_launcher_background)
        }
    }

    override fun getItemCount(): Int {
        return results.size
    }


    fun updateResults(newResults : List<EventDataItem>){
        results = newResults
        notifyDataSetChanged()
    }

    inner class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Initialize your views here
        val event_title: TextView = itemView.findViewById(R.id.event_title_result)
        val event_category: TextView = itemView.findViewById(R.id.event_category_result)
        val event_Image: ImageView = itemView.findViewById(R.id.event_image_result)
    }

}