//package com.example.aplicacionrestaurantes.ui.adapter

//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//
//class RestauranteAdapter(
//    private val restaurantes: MutableList<Restaurante>,
//    private val onEdit: (Restaurante) -> Unit,
//    private val onEliminarClick: (Restaurante) -> Unit
//
//) : RecyclerView.Adapter<RestauranteAdapter.RestauranteViewHolder>() {
//
//    class RestauranteViewHolder(private val binding: ItemRestauranteBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(restaurante: Restaurante, onEdit: (Restaurante) -> Unit, onEliminarClick: (Restaurante) -> Unit) {
//            binding.titulo.text = restaurante.titulo
//            binding.descripcion.text = restaurante.descripcion
//
//            val resId = binding.imagenRestaurante.context.resources.getIdentifier(restaurante.imagen, "drawable", binding.imagenRestaurante.context.packageName)
//            binding.imagenRestaurante.setImageResource(resId)
//
//            binding.imagenEliminar.setOnClickListener {
//                onEliminarClick(restaurante)
//            }
//
//            binding.imagenEditar.setOnClickListener {
//                onEdit(restaurante)
//            }
//
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestauranteViewHolder {
//        val binding = ItemRestauranteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return RestauranteViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: RestauranteViewHolder, position: Int) {
//        val restaurante = restaurantes[position]
//        holder.bind(restaurante, onEdit, onEliminarClick)
//    }
//
//    override fun getItemCount(): Int = restaurantes.size
//}