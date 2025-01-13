package com.example.mobile.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile.model.Mahasiswa
import com.example.mobile.repository.RepositoryMhs
import kotlinx.coroutines.launch


class InsertViewModel(
    private val mhs: RepositoryMhs
): ViewModel() {
    var uiEvent: InsertUiState by mutableStateOf(InsertUiState())
        private set
    var uiState: FormState by mutableStateOf(FormState.Idle)
        private set

    fun updateState(mahasiswaEvent: MahasiswaEvent){
        uiEvent = uiEvent.copy(
            insertUiEvent = mahasiswaEvent,
        )
    }

    fun validateFields(): Boolean{
        val event = uiEvent.insertUiEvent
        val errorState = FormErrorState(
            nim = if (event.nim.isNotEmpty()) null else "NIM tidak boleh kosong",
            nama = if (event.nama.isNotEmpty()) null else "Nama tidak boleh kosong",
            gender = if (event.gender.isNotEmpty()) null else "gender tidak boleh kosong",
            alamat = if (event.alamat.isNotEmpty()) null else "alamat tidak boleh kosong",
            kelas = if (event.kelas.isNotEmpty()) null else "kelas tidak boleh kosong",
            angkatan = if (event.angkatan.isNotEmpty()) null else "angkatan tidak boleh kosong",
            judul = if (event.judul.isNotEmpty()) null else "angkatan tidak boleh kosong",
            pembimbing1 = if (event.pembimbing1.isNotEmpty()) null else "pembimbing tidak boleh kosong",
            pembimbing2 = if (event.pembimbing2.isNotEmpty()) null else "pembimbing tidak boleh kosong",
        )
        uiEvent =  uiEvent.copy(isEntryValid = errorState)
        return errorState.isValid()
    }


    fun insertMhs(){
        if (validateFields()) {
            viewModelScope.launch {
                uiState = FormState.Loading
                try {
                    mhs.insertMhs(uiEvent.insertUiEvent.toMhsModel())
                    uiState = FormState.Success("Data berhasil Disimpan")
                }catch (e:Exception){
                    uiState = FormState.Error("Data gagal disimpan")
                }
            }
        }else{
            uiState = FormState.Error("Data tidak Valid")
        }
    }
    fun resetForm(){
        uiEvent = InsertUiState()
        uiState = FormState.Idle
    }
    fun resetSnackBarMessage(){
        uiState = FormState.Idle
    }
}

sealed class FormState {
    object Idle : FormState()
    object Loading : FormState()
    data class Success(val message: String) : FormState()
    data class Error(val message: String): FormState()
}

data class InsertUiState(
    val insertUiEvent: MahasiswaEvent = MahasiswaEvent(),
    val isEntryValid: FormErrorState = FormErrorState(),
)

data class FormErrorState(
    val nim: String? = null,
    val nama: String? = null,
    val gender: String? = null,
    val alamat: String? = null,
    val kelas: String? = null,
    val angkatan: String? = null,
    val judul: String? = null,
    val pembimbing1: String? = null,
    val pembimbing2: String? = null
){
    fun isValid(): Boolean {
        return nim == null && nama == null && gender == null &&
                alamat == null && kelas == null && angkatan == null && judul == null && pembimbing1 == null && pembimbing2 == null
    }
}

data class MahasiswaEvent(
    val nim: String = "",
    val nama: String = "",
    val gender: String = "",
    val alamat: String = "",
    val kelas: String = "",
    val angkatan: String = "",
    val judul: String = "",
    val pembimbing1: String = "",
    val pembimbing2: String = ""
)

fun MahasiswaEvent.toMhsModel(): Mahasiswa = Mahasiswa(
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














