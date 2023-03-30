package com.example.api

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.api.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: UserAdapter
    private val userNames = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = UserAdapter(userNames)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun getRetrofit (): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://localhost:7150/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun searchUser() {
        CoroutineScope(Dispatchers.IO).launch {
            val call : Response<List<UserResponse>> = getRetrofit().create(APIService::class.java).getUsers("Users")
            val puppies : List<UserResponse>? = call.body()
            runOnUiThread{
                if (call.isSuccessful){
                    val users = puppies?.listIterator()
                    adapter.addAll((users?.asSequence()?.map { it.toUser() }?.toList() ?: emptyList()) as List<User>)
                    adapter.notifyDataSetChanged()
                    //showRecyclerView
                }else
                {
                    showError()
                }
            }

        }
    }

    private fun showError() {
        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
    }
}