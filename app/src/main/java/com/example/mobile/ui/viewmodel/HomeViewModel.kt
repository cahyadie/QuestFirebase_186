package com.example.mobile.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.model.Mahasiswa
import com.example.mobile.repository.RepositoryMhs
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repoMhs:RepositoryMhs
): ViewModel(){
    var mhsUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    init {
        getMhs()
        deleteMhs(Mahasiswa())
    }

    fun getMhs(){
        viewModelScope.launch {
            repoMhs.getAllMhs().onStart {
                mhsUiState = HomeUiState.Loading
            }
                .onStart {
                    mhsUiState = HomeUiState.Loading
                }
                .catch {
                    mhsUiState = HomeUiState.Error(e = it)
                }
                .collect{
                    mhsUiState = if(it.isEmpty()){
                        HomeUiState.Error(Exception("Belum Ada Data"))
                    }else{
                        HomeUiState.Success(it)
                    }
                }
        }
    }

    fun deleteMhs(mahasiswa: Mahasiswa) {
        viewModelScope.launch {
            try {
                repoMhs.deleteMhs(mahasiswa)
            } catch (e: Exception) {
                mhsUiState = HomeUiState.Error(e)
            }
        }
    }
}

sealed class HomeUiState{
    object Loading : HomeUiState()
        data class Success(val data: List<Mahasiswa>) : HomeUiState()
    data class Error(val e: Throwable) : HomeUiState()
}