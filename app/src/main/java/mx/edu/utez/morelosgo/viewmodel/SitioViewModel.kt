package mx.edu.utez.morelosgo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import mx.edu.utez.morelosgo.data.network.model.Sitio
import mx.edu.utez.morelosgo.data.network.repository.SitioRepository

class SitioViewModel(application: Application) :
    AndroidViewModel(application) {
    
    val repository = SitioRepository(application.applicationContext)

    val sitios = MutableStateFlow<List<Sitio>>(emptyList())
    val errorMessage = MutableStateFlow("")

    init {
        getSitios()
    }

    fun getSitios() {
        repository.getAll(
            onSuccess = { lista ->
                sitios.value = lista
            },
            onError = { error ->
                errorMessage.value = error
            }
        )
    }

    fun insertSitio(sitio: Sitio) {
        repository.create(
            sitio = sitio,
            onSuccess = {
                getSitios() // Recargar lista después de insertar
            },
            onError = { error ->
                errorMessage.value = error
            }
        )
    }

    fun updateSitio(sitio: Sitio) {
        repository.update(
            sitio = sitio,
            onSuccess = {
                getSitios() // Recargar lista después de actualizar
            },
            onError = { error ->
                errorMessage.value = error
            }
        )
    }

    fun deleteSitio(idSitio: Int) {
        repository.delete(
            idSitio = idSitio,
            onSuccess = {
                getSitios() // Recargar lista después de eliminar
            },
            onError = { error ->
                errorMessage.value = error
            }
        )
    }
}
