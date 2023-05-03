package com.example.eticklets


import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebapostpdf.DownloadPdfTask
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Base64




class InicioActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var drawer: DrawerLayout
    private lateinit var toogle: ActionBarDrawerToggle
    private var respuestaQR: List<String> = emptyList()
    private lateinit var recyclerView: RecyclerView
    private lateinit var pdfAdapter: PdfAdapter

    @SuppressLint("MissingInflatedId", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        val btnSort: ImageButton = findViewById(R.id.btnSortComercio)
        val btnSortFecha: ImageButton = findViewById(R.id.btnSortFecha)
        val progressBar : ProgressBar = findViewById(R.id.progressBar)
        var pdfs: MutableList<PdfsGet> = mutableListOf()

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)

        toogle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawer.addDrawerListener(toogle)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)


        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            initScanner()
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://eticketsapi.azurewebsites.net/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val pdfApi = retrofit.create(PdfApi::class.java)
        lifecycleScope.launch {
            try {
                pdfs = pdfApi.getPdfs()
                Toast.makeText(this@InicioActivity, "Completado el get", Toast.LENGTH_LONG).show()
                progressBar.isVisible = false
                recyclerView = findViewById(R.id.pdfRecyclerView)
                btnSort.isVisible = true
                btnSortFecha.isVisible = true
                btnSort.setOnClickListener {
                    val listaux: List<PdfsGet> = pdfs
                    pdfs = listaux.sortedBy { it.titulo }.toMutableList()
                    pdfAdapter = PdfAdapter(pdfs, { pdfItem ->
                        val intent = Intent(applicationContext, PdfViewActivity::class.java)
                        intent.putExtra("pdfbase64", pdfItem.contenidoPDF)
                        startActivity(intent)
                    }, { pdfItem ->
                        val builder = android.app.AlertDialog.Builder(this@InicioActivity)
                        builder.setMessage("¿Está seguro que desea eliminar el PDF?")
                            .setCancelable(false)
                            .setPositiveButton("Sí") { _, _ ->
                                lifecycleScope.launch {
                                    try {
                                        pdfApi.deletePdf(pdfItem.idPdf)
                                        pdfs.remove(pdfItem)
                                        pdfAdapter.notifyDataSetChanged()
                                        Toast.makeText(
                                            this@InicioActivity,
                                            "PDF eliminado",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            this@InicioActivity,
                                            e.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                            .setNegativeButton("No") { dialog, _ ->
                                dialog.dismiss()
                            }
                        val alert = builder.create()
                        alert.show()
                    })
                    pdfAdapter.notifyDataSetChanged()
                    recyclerView.adapter = pdfAdapter
                    recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                }

                btnSortFecha.setOnClickListener {
                    val listaaux: List<PdfsGet> = pdfs
                    pdfs = listaaux.sortedByDescending { it.fechaSubida }.toMutableList()
                    pdfAdapter = PdfAdapter(pdfs, { pdfItem ->
                        val intent = Intent(applicationContext, PdfViewActivity::class.java)
                        intent.putExtra("pdfbase64", pdfItem.contenidoPDF)
                        startActivity(intent)
                    }, { pdfItem ->
                        val builder = android.app.AlertDialog.Builder(this@InicioActivity)
                        builder.setMessage("¿Está seguro que desea eliminar el PDF?")
                            .setCancelable(false)
                            .setPositiveButton("Sí") { _, _ ->
                                lifecycleScope.launch {
                                    try {
                                        pdfApi.deletePdf(pdfItem.idPdf)
                                        pdfs.remove(pdfItem)
                                        pdfAdapter.notifyDataSetChanged()
                                        Toast.makeText(
                                            this@InicioActivity,
                                            "PDF eliminado",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            this@InicioActivity,
                                            e.message,
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                }
                            }
                            .setNegativeButton("No") { dialog, _ ->
                                dialog.dismiss()
                            }
                        val alert = builder.create()
                        alert.show()
                    })
                    pdfAdapter.notifyDataSetChanged()
                    recyclerView.adapter = pdfAdapter
                    recyclerView.layoutManager = LinearLayoutManager(applicationContext)
                }

                val listaaux: List<PdfsGet> = pdfs
                pdfs = listaaux.sortedByDescending { it.fechaSubida }.toMutableList()
                pdfAdapter = PdfAdapter(pdfs, { pdfItem ->
                    val intent = Intent(applicationContext, PdfViewActivity::class.java)
                    intent.putExtra("pdfbase64", pdfItem.contenidoPDF)
                    startActivity(intent)
                }, { pdfItem ->
                    val builder = android.app.AlertDialog.Builder(this@InicioActivity)
                    builder.setMessage("¿Está seguro que desea eliminar el PDF?")
                        .setCancelable(false)
                        .setPositiveButton("Sí") { _, _ ->
                            lifecycleScope.launch {
                                try {
                                    pdfApi.deletePdf(pdfItem.idPdf)
                                    pdfs.remove(pdfItem)
                                    pdfAdapter.notifyDataSetChanged()
                                    Toast.makeText(
                                        this@InicioActivity,
                                        "PDF eliminado",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        this@InicioActivity,
                                        e.message,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }
                    val alert = builder.create()
                    alert.show()
                })
                recyclerView.adapter = pdfAdapter
                recyclerView.layoutManager = LinearLayoutManager(applicationContext)
            } catch (e: Exception) {
                Toast.makeText(this@InicioActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }




        firebaseAuth = Firebase.auth
    }


    private fun initScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("Escanea el código QR")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelaste el escanéo", Toast.LENGTH_SHORT).show()
            } else {
                respuestaQR = result.contents.split("*")
                val url = respuestaQR[0]
                DownloadPdfTask(object : DownloadPdfTask.DownloadPdfListener {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onDownloadComplete(pdfBytes: ByteArray) {
                        // El pdf se descarga y el array de bytes lo convertmos en un string de base 64
                        val pdfBase64 = Base64.getEncoder().encodeToString(pdfBytes)
                        val retrofit = Retrofit.Builder()
                            .baseUrl("https://eticketsapi.azurewebsites.net/api/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build()
                        //Instanciamos la llamada a la interfaz
                        val servicioAPI = retrofit.create(ServicioAPiPostPdf::class.java)
                        //Creamos el objeto pdf con el que vamos a enviar los datos a la base de datos
                        val pdf = PdfsPost(
                            titulo = respuestaQR[1],
                            contenidoPDF = pdfBase64,
                            emailUser = "elminikaki@gmail.com",
                        )
                        // Decodifica la cadena Base64 a un ByteArray
                        val decodedBytes =
                            android.util.Base64.decode(pdfBase64, android.util.Base64.DEFAULT)
                        //Este metodo es para realizar la llamada al metodo post
                        servicioAPI.crearPdf(pdf).enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                Log.d("MainActivity", "onResponse: ${response.body()}")
                                if (response.isSuccessful) {
                                    // La llamada fue exitosa, se ha introducido el pdf
                                    Toast.makeText(
                                        this@InicioActivity,
                                        "pdf insertado",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    //vuelve a cargar la actividad principal
                                    val intent =
                                        Intent(this@InicioActivity, InicioActivity::class.java)
                                    startActivity(intent)

                                } else {
                                    // La llamada falló por algún motivo
                                    Toast.makeText(
                                        this@InicioActivity,
                                        response.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                // La llamada falló debido a un error de red u otro tipo de error
                                Toast.makeText(
                                    this@InicioActivity,
                                    t.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                    }

                    override fun onDownloadFailed(errorMessage: String) {
                        // Maneja el error aquí
                        Toast.makeText(this@InicioActivity, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }).execute(url)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_Inicio -> {
                val intent: Intent = Intent(this, InicioActivity::class.java)
                startActivity(intent)
                finish()
            }

            R.id.menu_item_Escaner -> {
                initScanner()
            }

            R.id.menu_item_ayuda -> {
                Toast.makeText(this, "La ayuda nunca llegó", Toast.LENGTH_SHORT).show()
            }

            R.id.menu_item_cerrarSesion -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("¿Estás seguro que quieres cerrar sesión?")
                    .setCancelable(false)
                    .setIcon(R.drawable.peligro)
                    .setPositiveButton("Cerrar Sesión") { _, _ ->
                        cerrarSesion()
                    }
                    .setNegativeButton("Cancelar") { dialog, _ ->
                        dialog.cancel()
                    }
                val alert = builder.create()
                alert.show()
            }
        }

        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toogle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toogle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toogle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            return
        }
        return
    }


    private fun cerrarSesion() {
        firebaseAuth.signOut()
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }


}