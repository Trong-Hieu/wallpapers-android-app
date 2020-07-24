package com.example.wallpaper

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot

class WallpapersViewModel: ViewModel(){

    private val firebaseRepository: FirebaseRepository = FirebaseRepository()
    private val wallpapersList: MutableLiveData<List<WallpapersModel>> by lazy {
        MutableLiveData<List<WallpapersModel>>().also {
            loadWallpapersData()
        }
    }

    fun getWallpapersList(): LiveData<List<WallpapersModel>>{
        return wallpapersList
    }

    fun loadWallpapersData(){
        // Query data from repo
        firebaseRepository.queryWallpapers().addOnCompleteListener {
            if(it.isSuccessful){
                val result = it.result
                if (result!!.isEmpty){
                    // No more result to load, reach bottom of page
                }
                else{
                    // Result are ready to load
                    if (wallpapersList.value == null){
                        // Load first page
                        wallpapersList.value = result.toObjects(WallpapersModel::class.java)
                    }
                    else{
                        // Load next page
                        wallpapersList.value = wallpapersList.value!!.plus(result.toObjects(WallpapersModel::class.java))
                    }

//                    wallpapersList.value = result.toObjects(WallpapersModel::class.java)

                    // get the last Document
                    val lastItem : DocumentSnapshot = result.documents[result.size() - 1]
                    firebaseRepository.lastVisible = lastItem

                }

            }
            else{
                // Error
                Log.d("VIEW_MODEL_LOG", "Error: ${it.exception!!.message}")
            }
        }

    }

}