package com.example.gartenconnect.adapter

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.gartenconnect.R


class GalleryAdapter(private var images: List<String>, var context: Context?): RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {
    private var imagePosition = 0
    inner class GalleryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.gallery_item_image)

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    imagePosition = position
                    val image = images[position]
                    Log.i("GalleryAdapter", "Image: $image")
                    showImageAlertDialog(image)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.gallery_item, parent, false)
        return GalleryViewHolder(view)
    }

    override fun getItemCount() = images.size

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val image = images[position]
        Glide.with(holder.itemView.context)
            .load(image)
            .into(holder.image)
    }

    fun updateData(newData: List<String>){
        images = newData
        notifyDataSetChanged()
    }

    fun showImageAlertDialog(imageStr: String) {
    Log.i("GalleryAdapter", "Image: $imageStr")
        val builder = AlertDialog.Builder(context!!)

        // Inflar el diseño personalizado para el AlertDialog
        val view =
            LayoutInflater.from(context).inflate(R.layout.image_item, null)

        // Obtener la referencia al ImageView del diseño personalizado
        val imageView: ImageView = view.findViewById(R.id.imageView_image)

        Glide.with(context!!)
            .load(imageStr)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)

        // Cargar la imagen en el ImageView usando Glide u otra biblioteca de carga de imágenes

        // Establecer la vista personalizada en el AlertDialog
        builder.setView(view)

        // Mostrar el AlertDialog
        builder.show()
    }
}

