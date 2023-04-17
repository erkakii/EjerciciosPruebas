package com.example.pruebapostpdf

import android.os.AsyncTask
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL

class DownloadPdfTask(val listener: DownloadPdfListener): AsyncTask<String, Void, ByteArray>() {

    //Esta clase lo que consigue es que se pueda hacer una llamada a un servidor y descargar un pdf
    interface DownloadPdfListener {
        fun onDownloadComplete(pdfBytes: ByteArray)
        fun onDownloadFailed(errorMessage: String)
    }

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg urls: String): ByteArray? {
        try {
            val url = URL(urls[0])
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.doInput = true
            connection.connect()

            val inputStream = connection.inputStream
            val outputStream = ByteArrayOutputStream()

            val buffer = ByteArray(1024)
            var len: Int
            while (inputStream.read(buffer).also { len = it } != -1) {
                outputStream.write(buffer, 0, len)
            }

            outputStream.flush()
            return outputStream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    override fun onPostExecute(result: ByteArray?) {
        if (result != null) {
            listener.onDownloadComplete(result)
        } else {
            listener.onDownloadFailed("Failed to download PDF")
        }
    }
}
