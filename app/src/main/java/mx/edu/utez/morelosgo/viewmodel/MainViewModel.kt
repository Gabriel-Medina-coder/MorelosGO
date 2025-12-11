package mx.edu.utez.morelosgo.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mx.edu.utez.morelosgo.data.network.model.Favorito
import mx.edu.utez.morelosgo.data.network.model.Sitio
import mx.edu.utez.morelosgo.data.network.repository.FavoritoRepository
import mx.edu.utez.morelosgo.data.network.repository.SitioRepository
import mx.edu.utez.morelosgo.utils.SessionManager

class MainViewModel(context: Context) : ViewModel() {
    
    private val sitioRepository = SitioRepository(context)
    private val favoritoRepository = FavoritoRepository(context)
    private val appContext = context.applicationContext
    
    // State flows
    private val _sitios = MutableStateFlow<List<Sitio>>(emptyList())
    val sitios: StateFlow<List<Sitio>> = _sitios.asStateFlow()
    
    private val _favoritos = MutableStateFlow<List<Favorito>>(emptyList())
    val favoritos: StateFlow<List<Favorito>> = _favoritos.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _selectedSitio = MutableStateFlow<Sitio?>(null)
    val selectedSitio: StateFlow<Sitio?> = _selectedSitio.asStateFlow()
    
    init {
        loadSitios()
        loadFavoritos()
    }
    
    fun loadSitios() {
        _isLoading.value = true
        sitioRepository.getAll(
            onSuccess = { listaSitios ->
                _sitios.value = listaSitios
                _isLoading.value = false
            },
            onError = { error ->
                _errorMessage.value = "Error al cargar sitios: $error"
                _isLoading.value = false
            }
        )
    }
    
    fun loadFavoritos() {
        val currentUserId = SessionManager.getUserId(appContext)
        if (currentUserId != 0) {
            favoritoRepository.getAll(
                onSuccess = { listaFavoritos ->
                    _favoritos.value = listaFavoritos.filter { it.idUsuario == currentUserId }
                },
                onError = { }
            )
        }
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun getFilteredSitios(): List<Sitio> {
        return if (_searchQuery.value.isBlank()) {
            _sitios.value
        } else {
            _sitios.value.filter { 
                it.nombre.contains(_searchQuery.value, ignoreCase = true) ||
                it.tipo.contains(_searchQuery.value, ignoreCase = true)
            }
        }
    }
    
    fun isFavorite(idSitio: Int): Boolean {
        return _favoritos.value.any { it.idSitio == idSitio }
    }
    
    fun toggleFavorite(sitio: Sitio) {
        val esFavorito = isFavorite(sitio.idSitio)
        val currentUserId = SessionManager.getUserId(appContext)
        
        if (currentUserId == 0) {
            return
        }
        
        if (esFavorito) {
            // Remover favorito
            val favorito = _favoritos.value.find { it.idSitio == sitio.idSitio }
            favorito?.let {
                favoritoRepository.delete(
                    idFavorito = it.idFavorito,
                    onSuccess = { loadFavoritos() },
                    onError = { }
                )
            }
        } else {
            // Agregar favorito
            val nuevoFavorito = Favorito(
                idFavorito = 0,
                idUsuario = currentUserId,
                idSitio = sitio.idSitio
            )
            favoritoRepository.create(
                favorito = nuevoFavorito,
                onSuccess = { loadFavoritos() },
                onError = { }
            )
        }
    }
    
    fun selectSitio(sitio: Sitio?) {
        _selectedSitio.value = sitio
    }
    
    fun dismissDialog() {
        _selectedSitio.value = null
    }
}
