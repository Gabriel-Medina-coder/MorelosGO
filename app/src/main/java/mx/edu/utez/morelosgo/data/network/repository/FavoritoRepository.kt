package mx.edu.utez.morelosgo.data.network.repository

import android.content.Context
import mx.edu.utez.morelosgo.data.network.api.FavoritosAPI
import mx.edu.utez.morelosgo.data.network.dao.FavoritoDao
import mx.edu.utez.morelosgo.data.network.model.Favorito

class FavoritoRepository(context: Context) : FavoritoDao {

    private val api = FavoritosAPI(context)

    override fun getAll(
        onSuccess: (List<Favorito>) -> Unit,
        onError: (String) -> Unit
    ) {
        api.getAll(onSuccess, onError)
    }

    override fun create(
        favorito: Favorito,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        api.create(favorito, onSuccess, onError)
    }

    override fun update(
        favorito: Favorito,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        api.update(favorito, onSuccess, onError)
    }

    override fun delete(
        idFavorito: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        api.delete(idFavorito, onSuccess, onError)
    }
}
