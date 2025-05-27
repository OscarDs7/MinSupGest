package com.example.minsupgest

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.firestore.FirebaseFirestore


class GraficosActivity : AppCompatActivity() {
    //Instancias a componentes gráficos
    private lateinit var barChart: BarChart
    private lateinit var barChart2: BarChart
    private lateinit var btnRegreso: Button
    private lateinit var txtCanVen: TextView
    private lateinit var txtTotGan: TextView
    //Instancia a BD en Firebase Firestore
    private val db = FirebaseFirestore.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_graficos)

        //Vinculación
        barChart = findViewById(R.id.barChart)
        barChart2 = findViewById(R.id.barChart2)
        btnRegreso = findViewById(R.id.btnBack)
        txtCanVen = findViewById(R.id.txtCantVentas)
        txtTotGan = findViewById(R.id.txtTotalGanancia)

        //Subrayados de etiquetas
        txtCanVen.paintFlags = txtCanVen.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        txtTotGan.paintFlags = txtTotGan.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        //Llamadas a funciones
        cargarDatosVentas()
        cargarDatosGanancia()

        // Evento de regreso
        btnRegreso.setOnClickListener {
            onBackPressedDispatcher.onBackPressed() //Regreso a la ventana anterior
        }

    }
        // Función que carga los datos de ventas desde Firestore y los muestra en un gráfico de barras
        private fun cargarDatosVentas() {
            // Accede a la colección "ventas" en Cloud Firestore
            db.collection("ventas")
                .get()
                .addOnSuccessListener { result ->
                    // Lista para guardar las entradas del gráfico (valores numéricos)
                    val entries = ArrayList<BarEntry>()
                    // Lista para guardar las etiquetas (nombres de productos)
                    val labels = ArrayList<String>()

                    var index = 0 // Índice para posicionar las barras en el eje X

                    // Recorre todos los documentos obtenidos de la colección
                    for (document in result) {
                        // Obtiene el nombre del producto (campo "nombre_prod"), o "Producto" si está nulo
                        val nombre = document.getString("nombre_prod") ?: "Producto"
                        // Obtiene la cantidad de ventas (campo "cantidad_ventas") como float
                        val cantidad = document.getLong("cantidad_ventas")?.toFloat() ?: 0f

                        // Solo agrega productos con ventas mayores a cero
                        if (cantidad > 0f) {
                            entries.add(BarEntry(index.toFloat(), cantidad)) // Agrega la barra
                            labels.add(nombre) // Agrega la etiqueta correspondiente
                            index++ // Incrementa la posición en X
                        }
                    }

                    // Si no hay datos válidos, muestra un mensaje y termina
                    if (entries.isEmpty()) {
                        Toast.makeText(this, "No hay ventas registradas", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }

                    // Crea un conjunto de datos para el gráfico con las entradas
                    val dataSet = BarDataSet(entries, "Cantidad de ventas por producto")
                    dataSet.valueTextSize = 13f // Tamaño del texto de los valores sobre cada barra
                    dataSet.color = Color.BLUE // Color de las barras
                    dataSet.valueTextColor = Color.parseColor("#ffffff") // Color negro para los textos

                    // Asocia el conjunto de datos al gráfico
                    val data = BarData(dataSet)
                    barChart.data = data

                    // Configuración del eje X
                    val xAxis = barChart.xAxis
                    xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                    xAxis.granularity = 1f
                    xAxis.labelRotationAngle = -45f
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.setDrawLabels(true)

                    // Ejes y descripción
                    barChart.axisLeft.axisMinimum = 0f
                    barChart.axisRight.isEnabled = false
                    barChart.description.isEnabled = false

                    // Espaciado y presentación
                    barChart.setFitBars(true)
                    barChart.setExtraOffsets(10f, 16f, 10f, 35f) // Deja más espacio inferior para etiquetas giradas
                    barChart.setExtraTopOffset(16f)

                    barChart.animateY(1000)
                    barChart.invalidate()
                }
                .addOnFailureListener {
                    // Muestra mensaje si hubo un error al consultar Firestore
                    Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show()
                }
        }//cargarDatosVentas()


        private fun cargarDatosGanancia() {
        db.collection("ventas")
            .get()
            .addOnSuccessListener { result ->
                val entries = ArrayList<BarEntry>()
                val labels = ArrayList<String>()

                var index = 0
                for (document in result) {
                    val nombre = document.getString("nombre_prod") ?: "Producto"
                    val ganancia = document.getLong("total_ganancia")?.toFloat() ?: 0f

                    if (ganancia > 0f) {
                        entries.add(BarEntry(index.toFloat(), ganancia))
                        labels.add(nombre)
                        index++
                    }
                }

                if (entries.isEmpty()) {
                    Toast.makeText(this, "No hay ganancias registradas", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val dataSet = BarDataSet(entries, "Total de ganancia por producto") // Sin etiqueta si no la quieres mostrar
                val data = BarData(dataSet)

                dataSet.valueTextSize = 13f
                dataSet.color = Color.CYAN
                dataSet.valueTextColor = Color.WHITE

                barChart2.data = data

                // Configuración del eje X
                val xAxis = barChart2.xAxis
                xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                xAxis.granularity = 1f
                xAxis.labelRotationAngle = -45f
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawLabels(true)

                // Ejes y descripción
                barChart2.axisLeft.axisMinimum = 0f
                barChart2.axisRight.isEnabled = false
                barChart2.description.isEnabled = false

                // Espaciado y presentación
                barChart2.setFitBars(true)
                barChart2.setExtraOffsets(10f, 16f, 10f, 35f) // Deja más espacio inferior para etiquetas giradas
                barChart2.setExtraTopOffset(16f)

                barChart2.animateY(1000)
                barChart2.invalidate()

            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show()
            }
    }//cargarDatosGanancia

}