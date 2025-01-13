package com.example.mobile.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.model.Mahasiswa
import com.example.mobile.repository.RepositoryMhs
import com.example.mobile.ui.Navigation.DestinasiDetail
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class DetailViewModel (
    savedStatehandle: SavedStateHandle,
    private val mhs: RepositoryMhs
): ViewModel(){
    private val _nim: String = checkNotNull(savedStatehandle[DestinasiDetail.NIM])
    val detailUiState: StateFlow<DetailUiState> = mhs.getMhs(_nim)
        .filterNotNull()
        .map {
            DetailUiState(
                detailUiEvent = it.toDetailUiEvent(),
                isLoading = false,
            )
        }
        .onStart {
            emit(DetailUiState(isLoading = true))
            delay(600)
        }
        .catch {
            emit(
                DetailUiState(
                    isLoading = false,
                    isError = true,
                    errorMessage = it.message ?: "Terjadi Kesalahan",
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = DetailUiState(
                isLoading = true,
            ),
        )
    fun deleteMhs(){
        detailUiState.value.detailUiEvent.toMhsModel().let {
            viewModelScope.launch {
                mhs.deleteMhs(it)
            }
        }
    }
}

data class DetailUiState(
    val detailUiEvent: MahasiswaEvent = MahasiswaEvent(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
){
    val isUiEventEmpty: Boolean
        get() = detailUiEvent == MahasiswaEvent()
    val isUiEventNotEmpty: Boolean
        get() = detailUiEvent != MahasiswaEvent()
}
fun Mahasiswa.toDetailUiEvent():MahasiswaEvent {
    return MahasiswaEvent(
        nim = nim,
        nama = nama,
        gender = gender,
        alamat = alamat,
        kelas = kelas,
        angkatan = angkatan,
        judul = judul,
        pembimbing1 = pembimbing1,
        pembimbing2 = pembimbing2
    )
}