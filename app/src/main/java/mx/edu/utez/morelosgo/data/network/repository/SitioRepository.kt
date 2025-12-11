package mx.edu.utez.morelosgo.data.network.repository

import android.content.Context
import mx.edu.utez.morelosgo.data.network.api.SitiosAPI
import mx.edu.utez.morelosgo.data.network.dao.SitioDao
import mx.edu.utez.morelosgo.data.network.model.Sitio

class SitioRepository(context: Context) : SitioDao {

    private val api = SitiosAPI(context)

    override fun getAll(
        onSuccess: (List<Sitio>) -> Unit,
        onError: (String) -> Unit
    ) {
        api.getAll(onSuccess, onError)
    }

    override fun create(
        sitio: Sitio,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        api.create(sitio, onSuccess, onError)
    }

    override fun update(
        sitio: Sitio,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        api.update(sitio, onSuccess, onError)
    }

    override fun delete(
        idSitio: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        api.delete(idSitio, onSuccess, onError)
    }
}
