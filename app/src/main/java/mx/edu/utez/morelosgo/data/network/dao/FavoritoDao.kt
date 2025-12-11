package mx.edu.utez.morelosgo.data.network.dao

import mx.edu.utez.morelosgo.data.network.model.Favorito

interface FavoritoDao {
    fun getAll(
        onSuccess: (List<Favorito>) -> Unit,
        onError: (String) -> Unit
    )

    fun create(
        favorito: Favorito,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    fun update(
        favorito: Favorito,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    fun delete(
        idFavorito: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )
}