package com.example.drawer.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.drawer.entity.Pixabay
import com.example.drawer.entity.Results
import com.example.drawer.service.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
private const val TAG="HomeViewModel"
class HomeViewModel: ViewModel() {
    private val _data=MutableLiveData<List<Pixabay>>()
    val data:LiveData<List<Pixabay>> get() = _data
    private val _keyWord=MutableLiveData<String>("girl")
    val keyWord:LiveData<String> get() = _keyWord
    suspend fun getData(){
//        Log.d(TAG, "getData: ${Service.retrofitService.getPixabayList("girl")}")
      _data.value = _keyWord.value?.let { Service.retrofitService.getPixabayList(it).hits }
    }
    fun setKeyWord(value:String){
        _keyWord.value=value
    }
}