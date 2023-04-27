package com.example.metodogetpdfs

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale



class PdfAdapter(private val pdfList: List<Pdf>, private val onItemClickListener: (Pdf) -> Unit) : RecyclerView.Adapter<PdfAdapter.PdfViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PdfViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pdf_item, parent, false)
        return PdfViewHolder(view)
    }

    override fun getItemCount() = pdfList.size

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PdfViewHolder, position: Int) {
        val pdf = pdfList[position]
        holder.bind(pdf)
        holder.itemView.setOnClickListener { onItemClickListener(pdf) }
    }

    class PdfViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvPdfTitle)
        private val tvPdfDate: TextView = itemView.findViewById(R.id.tvPdfDate)
        private val ivPdfImage: ImageView = itemView.findViewById(R.id.ivPdfImage)

        private val titleToImageMap : Map<String, Int> = mapOf(
            "Mercadona" to R.drawable.mercadona,
            "El Corte Inglés" to R.drawable.elcorteingles
        )

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(pdf: Pdf) {
            val fechaPdf : List<String> = pdf.fechaSubida.split("T")
            tvTitle.text = pdf.titulo
            tvPdfDate.text = fechaPdf[0]
            ivPdfImage.setImageResource(titleToImageMap[pdf.titulo] ?: R.drawable.carrito)
        }

    }
}
