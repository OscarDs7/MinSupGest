package com.example.minsupgest

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.firestore.FirebaseFirestore


class GraficosActivity : AppCompatActivity() {
    //Instancias
    private lateinit var barChart: BarChart
    private lateinit var barChart2: BarChart
    private val db = FirebaseFirestore.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_graficos)

        //Vinculación
        barChart = findViewById(R.id.barChart)
        barChart2 = findViewById(R.id.barChart2)

        //Llamada a función
        cargarDatosVentas()
        cargarDatosGanancia()

    }

    private fun cargarDatosVentas() {
        db.collection("ventas")
            .get()
            .addOnSuccessListener { result ->
                val entries = ArrayList<BarEntry>()
                val labels = ArrayList<String>()

                var index = 0
                for (document in result) {
                    val nombre = document.getString("nombre_prod") ?: "Producto"
                    val cantidad = document.getLong("cantidad_ventas")?.toFloat() ?: 0f

                    if (cantidad > 0f) {
                        entries.add(BarEntry(index.toFloat(), cantidad))
                        labels.add(nombre)
                        index++
                    }
                }

                if (entries.isEmpty()) {
                    Toast.makeText(this, "No hay ventas registradas", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val dataSet = BarDataSet(entries, "Ventas por producto")
                val data = BarData(dataSet)
                dataSet.valueTextSize = 12f // Tamaño de los números de cada barra

                barChart.data = data
                barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                barChart.xAxis.granularity = 1f
                barChart.xAxis.setDrawLabels(true)
                barChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
                barChart.axisLeft.axisMinimum = 0f
                barChart.description.isEnabled = false

                barChart.invalidate()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show()
            }
    }//cargarDatosVentas

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

                val dataSet = BarDataSet(entries, "Total de ganancia por producto")
                val data = BarData(dataSet)
                dataSet.valueTextSize = 12f // Tamaño de los números de cada barra


                barChart2.data = data
                barChart2.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
                barChart2.xAxis.granularity = 1f
                barChart2.xAxis.setDrawLabels(true)
                barChart2.xAxis.position = XAxis.XAxisPosition.BOTTOM
                barChart2.axisLeft.axisMinimum = 0f
                barChart2.description.isEnabled = false

                barChart2.invalidate()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show()
            }
    }

}