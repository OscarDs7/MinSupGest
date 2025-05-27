package com.example.minsupgest

import android.os.Bundle
import android.graphics.Color
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.components.XAxis
import com.google.firebase.firestore.FirebaseFirestore

class RecomendacionesActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var barChart: BarChart
    private lateinit var tablaRecomendaciones: TableLayout
    private lateinit var resumenTexto: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recomendaciones)

        db = FirebaseFirestore.getInstance()
        barChart = findViewById(R.id.barChartRecomendaciones)
        tablaRecomendaciones = findViewById(R.id.tblRecomendaciones)
        resumenTexto = findViewById(R.id.txtResumenRecomendacion)

        cargarDatosVentas()
        val btnVolver = findViewById<Button>(R.id.btnVolverAlMenu)
        btnVolver.setOnClickListener {
            finish()  // Esto simplemente cierra la actividad actual y vuelve al menú anterior
        }

    }

    private fun cargarDatosVentas() {
        db.collection("ventas")
            .get()
            .addOnSuccessListener { result ->
                val conteoVentas = mutableMapOf<String, Float>()

                for (document in result) {
                    val nombre = document.getString("nombre_prod") ?: "Producto"
                    val cantidad = document.getLong("cantidad_ventas")?.toFloat() ?: 0f
                    if (cantidad > 0f) {
                        conteoVentas[nombre] = conteoVentas.getOrDefault(nombre, 0f) + cantidad
                    }
                }

                if (conteoVentas.isEmpty()) {
                    resumenTexto.text = "No hay ventas registradas."
                    return@addOnSuccessListener
                }

                mostrarGrafica(conteoVentas)
                mostrarTabla(conteoVentas)
                mostrarResumenYRecomendacion(conteoVentas)
            }
            .addOnFailureListener {
                resumenTexto.text = "Error al obtener datos de ventas."
            }
    }

    private fun mostrarGrafica(conteo: Map<String, Float>) {
        val entries = ArrayList<BarEntry>()
        val labels = ArrayList<String>()

        conteo.entries.forEachIndexed { index, entry ->
            entries.add(BarEntry(index.toFloat(), entry.value))
            labels.add(entry.key)
        }

        val dataSet = BarDataSet(entries, "Ventas por producto")
        dataSet.color = Color.parseColor("#3F51B5")
        dataSet.valueTextSize = 13f // Tamaño del texto de los valores sobre cada barra

        val data = BarData(dataSet)
        data.barWidth = 0.9f

        barChart.data = data
        barChart.setFitBars(true)
        barChart.description.isEnabled = false

        val xAxis = barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = -45f

        barChart.axisRight.isEnabled = false
        barChart.animateY(1000)
        barChart.invalidate()
    }

    private fun mostrarTabla(conteo: Map<String, Float>) {
        for ((producto, cantidad) in conteo) {
            val fila = TableRow(this)

            val txtProducto = TextView(this).apply {
                text = producto
                setPadding(16, 8, 16, 8)
            }

            val txtCantidad = TextView(this).apply {
                text = cantidad.toInt().toString()
                setPadding(16, 8, 16, 8)
            }

            fila.addView(txtProducto)
            fila.addView(txtCantidad)
            tablaRecomendaciones.addView(fila)
        }
    }

    private fun mostrarResumenYRecomendacion(conteo: Map<String, Float>) {
        val masVendido = conteo.maxByOrNull { it.value }
        val menosVendido = conteo.minByOrNull { it.value }

        if (masVendido == null || menosVendido == null) {
            resumenTexto.text = "No se pudieron calcular los productos más y menos vendidos."
            return
        }

        db.collection("productos")
            .whereEqualTo("nombre_prod", masVendido.key)
            .get()
            .addOnSuccessListener { documentos ->
                val stock = documentos.firstOrNull()?.getLong("stock")?.toInt() ?: 0

                val texto = StringBuilder()
                texto.append("📈 Producto más vendido: ${masVendido.key} (${masVendido.value.toInt()} ventas)\n")
                texto.append("📉 Producto menos vendido: ${menosVendido.key} (${menosVendido.value.toInt()} ventas)\n\n")

                if (stock < 10) {
                    texto.append("🛒 Reponer '${masVendido.key}': quedan solo $stock unidades.")
                } else {
                    texto.append("✅ Stock de '${masVendido.key}' es suficiente: $stock unidades.")
                }

                resumenTexto.text = texto.toString()
            }
            .addOnFailureListener {
                resumenTexto.text = "Error al consultar stock del producto más vendido."
            }
    }
}
