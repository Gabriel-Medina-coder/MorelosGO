package mx.edu.utez.morelosgo.data.network.dao

import mx.edu.utez.morelosgo.data.network.model.Sitio

interface SitioDao {
    fun getAll(
        onSuccess: (List<Sitio>) -> Unit,
        onError: (String) -> Unit
    )

    fun create(
        sitio: Sitio,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    fun update(
        sitio: Sitio,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    fun delete(
        idSitio: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )
}