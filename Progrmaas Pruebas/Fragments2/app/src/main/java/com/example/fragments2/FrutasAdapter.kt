package com.example.fragments2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FrutasAdapter(private val frutas: List<String>) : RecyclerView.Adapter<FrutasAdapter.FrutaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FrutaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_fruta, parent, false)
        return FrutaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FrutaViewHolder, position: Int) {
        holder.bind(frutas[position])
    }

    override fun getItemCount(): Int {
        return frutas.size
    }

    class FrutaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val nombreFruta: TextView = itemView.findViewById(R.id.nombre_fruta)

        fun bind(fruta: String) {
            nombreFruta.text = fruta
        }
    }
}
