package mx.edu.utez.morelosgo.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import mx.edu.utez.morelosgo.data.network.model.Usuario
import mx.edu.utez.morelosgo.data.network.repository.FavoritoRepository
import mx.edu.utez.morelosgo.utils.SessionManager

class PerfilViewModel(context: Context) : ViewModel() {
    
    private val favoritoRepository = FavoritoRepository(context)
    private val appContext = context.applicationContext
    
    // State flows
    private val _currentUser = MutableStateFlow<Usuario?>(null)
    val currentUser: StateFlow<Usuario?> = _currentUser.asStateFlow()
    
    private val _favoritosCount = MutableStateFlow(0)
    val favoritosCount: StateFlow<Int> = _favoritosCount.asStateFlow()
    
    init {
        loadUserData()
        loadFavoritosCount()
    }
    
    private fun loadUserData() {
        _currentUser.value = SessionManager.getCurrentUser(appContext)
    }
    
    private fun loadFavoritosCount() {
        _currentUser.value?.let { user ->
            favoritoRepository.getAll(
                onSuccess = { favoritos ->
                    _favoritosCount.value = favoritos.count { it.idUsuario == user.idUsuario }
                },
                onError = { }
            )
        }
    }
    
    fun logout() {
        SessionManager.clearSession(appContext)
    }
}
