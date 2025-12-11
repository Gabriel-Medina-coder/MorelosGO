package mx.edu.utez.morelosgo.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import mx.edu.utez.morelosgo.data.network.model.Favorito
import mx.edu.utez.morelosgo.data.network.model.Sitio
import mx.edu.utez.morelosgo.data.network.repository.FavoritoRepository
import mx.edu.utez.morelosgo.data.network.repository.SitioRepository
import mx.edu.utez.morelosgo.utils.SessionManager

class MapViewModel(context: Context) : ViewModel() {
    
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
    
    private val _selectedFilter = MutableStateFlow("General")
    val selectedFilter: StateFlow<String> = _selectedFilter.asStateFlow()
    
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
            onError = {
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
    
    fun setFilter(filter: String) {
        _selectedFilter.value = filter
    }
    
    fun getFilteredSitios(): List<Sitio> {
        return when (_selectedFilter.value) {
            "Favoritos" -> {
                val favoritoIds = _favoritos.value.map { it.idSitio }.toSet()
                _sitios.value.filter { it.idSitio in favoritoIds }
            }
            else -> _sitios.value // "General" muestra todos
        }
    }
    
    fun isFavorite(idSitio: Int): Boolean {
        return _favoritos.value.any { it.idSitio == idSitio }
    }
    
    fun selectSitio(sitio: Sitio?) {
        _selectedSitio.value = sitio
    }
    
    fun dismissDialog() {
        _selectedSitio.value = null
    }
}
