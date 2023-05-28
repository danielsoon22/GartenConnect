package com.example.gartenconnect.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gartenconnect.model.GalleryRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GalleryViewModel: ViewModel(){
    private val _gallery = MutableLiveData<List<String>>()
    val galleryLiveData: MutableLiveData<List<String>> = _gallery
    private val galleryRepository: GalleryRepository = GalleryRepository()

    fun getImages(classId: String){
       viewModelScope.launch {
           try {
               val storageReferences = galleryRepository.getImages(classId)
               val imageUrls = mutableListOf<String>()
               for (storageRef in storageReferences) {
                   val url = storageRef.downloadUrl.await().toString()
                   imageUrls.add(url)
               }
               _gallery.value = imageUrls
           }catch (e: Error){
               Log.e("GalleryViewModel", "Error al obtener las imagenes: ${e.message}")
           }
       }
    }

    fun uploadImages(image: ByteArray, classId:String){
        viewModelScope.launch {
            try {
                val imageUrl = galleryRepository.uploadImages(image, classId)
                val imageUrls = mutableListOf<String>()
                imageUrls.add(imageUrl)
                _gallery.value = imageUrls
            }catch (e: Error){
                Log.e("GalleryViewModel", "Error al subir las imagenes: ${e.message}")
            }
        }
    }



}