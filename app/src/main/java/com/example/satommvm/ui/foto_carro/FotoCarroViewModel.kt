package com.example.satommvm.ui.foto_carro

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.satommvm.data.model.FotoCarro
import com.example.satommvm.data.repository.FotoCarroRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File

class FotoCarroViewModel(private val repository: FotoCarroRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(FotoCarroUiState())
    val uiState: StateFlow<FotoCarroUiState> = _uiState.asStateFlow()

    fun carregarFotos(carroId: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            repository.getFotosPorCarro(carroId)
                .catch { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
                .collect { lista -> _uiState.update { it.copy(isLoading = false, fotos = lista) } }
        }
    }

    fun adicionarFoto(context: Context, carroId: Int, imagemUriString: String?) {
        if (imagemUriString == null) {
            _uiState.update { it.copy(errorMessage = "Imagem inválida") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isAddingFoto = true) }
            try {
                // Lógica para guardar a foto (movemos para o ViewModel)
                val caminhoImagem = saveImageToInternalStorage(context, Uri.parse(imagemUriString), "foto_${carroId}_${System.currentTimeMillis()}.jpg")
                val novaFoto = FotoCarro(carroId = carroId, imagemUri = caminhoImagem)
                repository.adicionarFoto(novaFoto)
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            } finally {
                _uiState.update { it.copy(isAddingFoto = false) }
            }
        }
    }

    fun deletarFoto(foto: FotoCarro) {
        viewModelScope.launch {
            try {
                repository.deletarFoto(foto)
                // Apaga o ficheiro do armazenamento interno
                File(foto.imagemUri).delete()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message) }
            }
        }
    }

    fun resetErrorState() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private fun saveImageToInternalStorage(context: Context, uri: Uri, fileName: String): String {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.filesDir, fileName)
        inputStream.use { input -> file.outputStream().use { output -> input?.copyTo(output) } }
        return file.absolutePath
    }
}