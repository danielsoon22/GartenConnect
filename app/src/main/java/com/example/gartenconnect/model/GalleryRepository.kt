package com.example.gartenconnect.model

import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

class GalleryRepository {
    val storage = FirebaseStorage.getInstance()

    suspend fun getImages(classId: String): List<StorageReference> {
        val folderReference = storage.reference.child("classes").child(classId)
        return folderReference.listAll().await().items
    }

    suspend fun uploadImages(image: ByteArray, classId:String): String{
        val folderReference = storage.reference.child("classes").child(classId)

        val imageReference = folderReference.child("${System.currentTimeMillis()}.jpg")
        val uploadTask = imageReference.putBytes(image).await()
        return imageReference.downloadUrl.await().toString()
    }
}