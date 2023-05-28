package com.example.gartenconnect.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gartenconnect.model.BlogPost
import com.example.gartenconnect.model.BlogRepository

class BlogViewModel : ViewModel(){
    private val repository: BlogRepository = BlogRepository()
    private val TAG = "Log:BlogVM"
    private val _blogs = MutableLiveData<List<BlogPost>>()
    val blogLiveData: LiveData<List<BlogPost>> = _blogs

    fun fetchBlogs() {
        repository.getBlogs().addOnSuccessListener { documents ->
            val blogs = documents.mapNotNull { it.toObject(BlogPost::class.java) }
            _blogs.value = blogs
        }
    }


}