package com.example.minsupgest

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.firestore.FirebaseFirestore

class ventasActiviti : AppCompatActivity() {
    private lateinit var idproducto:EditText
    private lateinit var cantidad:EditText
    private lateinit var vender:Button
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ventas_activiti)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        idproducto = findViewById(R.id.edtidproducto)
        cantidad = findViewById(R.id.edtCantidad)
        vender = findViewById(R.id.btnventas)
        val db = FirebaseFirestore.getInstance()
        val productosVRef = db.collection("ventas")
        val productosRef = db.collection("productos")


        vender.setOnClickListener {
            val idBuscada = idproducto.text.toString()
            db.collection("productos").document(idBuscada)
                .get()
                .addOnSuccessListener { documento ->
                    if (documento.exists()) {
                        val Idpro = documento.id
                        val Nombre = documento.getString("nombre_prod")
                        val precioventa = documento.getDouble("precio_emp")?:0.0
                        val precioprovedor = documento.getDouble("precio_prov")?:0.0
                        val vendido = cantidad.text.toString().toInt()
                        val ganancia = precioventa - precioprovedor
                        val total  = ganancia*vendido
                        val nuevoDoc = hashMapOf(
                            "cantidad_ventas" to vendido,
                            "idproducto" to Idpro,
                            "nombre_prod" to Nombre,
                            "precio_emp" to precioventa,
                            "precio_prov" to precioprovedor,
                            "total_ganancia" to total

                        )
                        db.collection("ventas")
                            .add(nuevoDoc)
                            .addOnSuccessListener { docRef ->
                                Toast.makeText(this, "Dato registrado", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, "Dato no Registrado", Toast.LENGTH_SHORT).show()
                            }

                    } else {
                        Toast.makeText(this, "Dato no encontrado", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Fallo en la conexion ", Toast.LENGTH_SHORT).show()
                }


        }



    }

}