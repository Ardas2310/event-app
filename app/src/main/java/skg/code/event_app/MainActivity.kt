package skg.code.event_app

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "http://localhost:3001/"
class MainActivity : AppCompatActivity() {

//    private lateinit var recyclerView: RecyclerView
//    private lateinit var searchView: SearchView
    private lateinit var textViewId: TextView

    // Apo Chat
    private lateinit var adapter: LiveDataAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setContentView(R.layout.testing)

        //How to use Retrofit to fetch data from our REST API
        //TODO: Na to prosarmosoyme sta dika mas data
        //textViewId = findViewById(R.id.textViewId)
        recyclerView = findViewById(R.id.recyclerView)
        adapter = LiveDataAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        getMyData()

//        recyclerView = findViewById(R.id.recyclerView)
//        searchView = findViewById(R.id.searchView)
//
//
//        recyclerView.setHasFixedSize(true)
//        recyclerView.layoutManager = LinearLayoutManager(this)

//        addDataToList()
    }

//    private fun addDataToList(){
//
//    }


//    private fun getMyData() {
//        val retrofitBuilder = Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .baseUrl(BASE_URL)
//            .build()
//            .create(ApiInterface::class.java)
//
//        //.getEvents is in our interface
//        val retrofitData = retrofitBuilder.getEvents()
//
//        retrofitData.enqueue(object : Callback<List<LiveData>?> {
//            // To ena ? einai gia nullable. Ama einai null to variable,
//            // to app den crasharei me null pointer exception
//            // kai synexizei na douleuei an kai einai null.
//            override fun onResponse(
//                call: Call<List<LiveData>?>,
//                response: Response<List<LiveData>?>
//            ) {
//                val responseBody = response.body()!! // Ta dyo !! einai null safety
//
//                val myStringBuilder = StringBuilder()
//                for(myData in responseBody){
//                    myStringBuilder.append(myData.event_description)
//                    myStringBuilder.append("\n")
//                }
//                //Synthetic binding
//                textViewId.text = myStringBuilder.toString()
//
//
//            }
//
//            override fun onFailure(call: Call<List<LiveData>?>, t: Throwable) {
//                Log.d("testing", "onFailure: ${t.message}")
//            }
//        })
//    }

    private fun getMyData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterface::class.java)

        val retrofitData = retrofitBuilder.getEvents()

        retrofitData.enqueue(object : Callback<List<LiveData>?> {
            override fun onResponse(
                call: Call<List<LiveData>?>,
                response: Response<List<LiveData>?>
            ) {
                val responseBody = response.body()
                responseBody?.let {
                    adapter.updateEvents(it)
                }
            }

            override fun onFailure(call: Call<List<LiveData>?>, t: Throwable) {
                Log.d("testing", "onFailure: ${t.message}")
            }
        })
    }


}