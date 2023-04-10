package com.example.financo.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.financo.model.Country
import com.example.financo.model.UserModel
import com.example.financo.repository.UserRepository
import com.example.financo.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeActivityViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    lateinit var countryList: List<Country>
    val loadUserDataLiveData = MutableLiveData<Resource<UserModel>>()

    fun loadUserData(userName: String) {
        loadUserDataLiveData.postValue(Resource.loading(null))
        viewModelScope.launch {
            val result = repository.loadUserData(userName)
            if (result.data != null) {
                countryList = result.data.country
            }
            loadUserDataLiveData.postValue(result)
        }
    }
}