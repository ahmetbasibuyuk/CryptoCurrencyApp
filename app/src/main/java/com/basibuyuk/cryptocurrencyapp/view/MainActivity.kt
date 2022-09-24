package com.basibuyuk.cryptocurrencyapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basibuyuk.cryptocurrencyapp.R
import com.basibuyuk.cryptocurrencyapp.adapter.RecyclerViewAdapter
import com.basibuyuk.cryptocurrencyapp.model.CryptoModel
import com.basibuyuk.cryptocurrencyapp.service.CryptoAPI
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.Listener {

    private val BASE_URL = "https://raw.githubusercontent.com/"
    private var cryptoModels: ArrayList<CryptoModel>? = null
    private var recyclerViewAdapter: RecyclerViewAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //RecyclerView
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager



        loadData()


    }

    private fun loadData() {

        // Retrofit Object
        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()

        // Connector API and Retrofit
        val service = retrofit.create(CryptoAPI::class.java)

        // Calling the "Call" object
        val call = service.getData()

        // Create enqueue -> send the request and notify callback
        call.enqueue(object : Callback<List<CryptoModel>> {
            override fun onResponse(
                call: Call<List<CryptoModel>>, response: Response<List<CryptoModel>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        cryptoModels = ArrayList(it)


                        cryptoModels?.let {
                            recyclerViewAdapter = RecyclerViewAdapter(it, this@MainActivity)
                            recyclerView.adapter = recyclerViewAdapter
                        }

                    }
                }
            }

            override fun onFailure(call: Call<List<CryptoModel>>, t: Throwable) {
                t.printStackTrace()
            }

        })


    }

    override fun onItemClick(cryptoModel: CryptoModel) {
        Toast.makeText(this, "Clicked: ${cryptoModel.currency}", Toast.LENGTH_SHORT).show()
    }



    //Menu Create function
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    // Menu item function
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.info -> {
                Toast.makeText(this, "up-to-date data are not used.", Toast.LENGTH_LONG).show()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

}