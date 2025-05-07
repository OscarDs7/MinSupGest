package com.example.minsupgest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmpleadoAdapter(private val empleados: List<Empleado>) :
    RecyclerView.Adapter<EmpleadoAdapter.EmpleadoViewHolder>() {

    class EmpleadoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombre: TextView = view.findViewById(R.id.tvNombre)
        val tvApellido: TextView = view.findViewById(R.id.tvApellido)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmpleadoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_empleado, parent, false)
        return EmpleadoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmpleadoViewHolder, position: Int) {
        val empleado = empleados[position]
        holder.tvNombre.text = empleado.nombre
        holder.tvApellido.text = empleado.apellido
    }

    override fun getItemCount(): Int = empleados.size
}
