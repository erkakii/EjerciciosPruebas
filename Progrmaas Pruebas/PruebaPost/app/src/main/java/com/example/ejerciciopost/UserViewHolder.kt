package com.example.ejerciciopost

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.ejerciciopost.databinding.UserItemBinding

class UserViewHolder(view:View) : RecyclerView.ViewHolder(view) {

    private val binding = UserItemBinding.bind(view)



    fun bind(user: User) {
        binding.cardName.text = user.name
    }
}