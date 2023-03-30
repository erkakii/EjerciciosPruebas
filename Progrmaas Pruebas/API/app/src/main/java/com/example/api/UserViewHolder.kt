package com.example.api

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.api.databinding.UserItemBinding

class UserViewHolder(view : View): RecyclerView.ViewHolder(view){
    private val binding = UserItemBinding.bind(view)

    fun bind(user: User){
        binding.email.text = user.nombre

    }
}