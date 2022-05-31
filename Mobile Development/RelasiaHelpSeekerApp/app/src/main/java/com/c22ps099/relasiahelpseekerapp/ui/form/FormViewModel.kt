package com.c22ps099.relasiahelpseekerapp.ui.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.c22ps099.relasiahelpseekerapp.ui.home.PostsViewModel
import com.c22ps099.relasiahelpseekerapp.utils.itemsKab

class FormViewModel(private val token: String) : ViewModel() {
    private val _province = MutableLiveData<String>()
    val province: LiveData<String> = _province

    private val _cities = MutableLiveData<Array<String>>()
    val cities: LiveData<Array<String>> = _cities

    fun updateProvince(prov: String){
        _province.value=prov
    }
    fun getKabs(prov: String): Array<String>{
        return itemsKab(prov)
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val token: String) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return FormViewModel(token) as T
        }
    }
}