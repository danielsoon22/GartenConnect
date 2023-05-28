package com.example.gartenconnect.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gartenconnect.R
import com.example.gartenconnect.fragment.BlogDetailFragment
import com.example.gartenconnect.model.BlogPost
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BlogAdapter(var blogList: List<BlogPost>, private val fragmentManager: FragmentManager): RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {
    inner class BlogViewHolder(itemView: ViewGroup): RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.blog_title)
        val content: TextView = itemView.findViewById(R.id.blog_description)
        val date: TextView = itemView.findViewById(R.id.blog_date)
        val photo: ImageView = itemView.findViewById(R.id.blog_image)

        init {
            itemView.setOnClickListener {
                val blog = bindingAdapterPosition
                if (blog != RecyclerView.NO_POSITION) {
                    val blogPost = blogList[blog]
                    val blogDetailFragment = BlogDetailFragment().apply {
                        arguments = Bundle().apply {
                            putParcelable("blogPost", blogPost)
                        }
                    }
                    // Aquí necesitas una referencia a tu FragmentManager para realizar la transacción de fragmentos
                    // Puede que necesites pasarla al adaptador o encontrar otra forma de obtenerla
                    blogDetailFragment.show(fragmentManager, "BlogDetailFragment")
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogAdapter.BlogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.blog_item, parent, false)
        return BlogViewHolder(view as ViewGroup)
    }

    override fun onBindViewHolder(holder: BlogAdapter.BlogViewHolder, position: Int) {
        val blog = blogList[position]
        val timestamp = blog.date
        val milliseconds = timestamp!!.seconds * 1000 + timestamp.nanoseconds / 1000000
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        holder.title.text = blog.title
        holder.content.text = blog.content
        holder.date.text = sdf.format(Date(milliseconds))
        Glide.with(holder.itemView.context)
            .load(blog.photoUrl)
            .centerCrop()
            .into(holder.photo)
    }

    override fun getItemCount(): Int = blogList.size
}