package com.example.metodogetpdfs

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale



class PdfAdapter(private val pdfList: MutableList<Pdf>, private val onItemClickListener: (Pdf) -> Unit, private val onDeleteClickListener: (Pdf) -> Unit) : RecyclerView.Adapter<PdfAdapter.PdfViewHolder>() {

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
        holder.deleteButton.setOnClickListener { onDeleteClickListener(pdf) }

    }

    class PdfViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvPdfTitle)
        private val tvPdfDate: TextView = itemView.findViewById(R.id.tvPdfDate)
        private val ivPdfImage: ImageView = itemView.findViewById(R.id.ivPdfImage)
        val deleteButton : ImageButton = itemView.findViewById(R.id.btnDeletePdf)


        private val titleToImageMap : Map<String, Int> = mapOf(
            "Mercadona" to R.drawable.mercadona,
            "El Corte Inglés" to R.drawable.elcorteingles,
            "Álvaro Moreno" to R.drawable.alvaromoreno,
            "BP" to R.drawable.bp,
            "Carrefour" to R.drawable.carrefour,
            "Cepsa" to R.drawable.cepsa,
            "Decathlon" to R.drawable.decatlon,
            "Dia" to R.drawable.dia,
            "Eroski" to R.drawable.eroski,
            "Repsol" to R.drawable.repsol,
            "Shell" to R.drawable.shell,
            "Spar" to R.drawable.spar,
            "Worten" to R.drawable.worten,
            "Zara" to R.drawable.zara,
            "Lidl" to R.drawable.lidl,
            "Aldi" to R.drawable.aldi,
            "Ikea" to R.drawable.ikea,
            "MediaMarkt" to R.drawable.mediamarkt,
            "Alcampo" to R.drawable.alcampo,
            "Leroy Merlin" to R.drawable.leroymerlin,
            "Conforama" to R.drawable.conforama,
            "Breshka" to R.drawable.breshka,
            "C&A" to R.drawable.cya,
            "McDonald's" to R.drawable.macdonal,
            "Burger King" to R.drawable.burguerking,
            "KFC" to R.drawable.kfc
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
