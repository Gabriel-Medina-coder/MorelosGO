package mx.edu.utez.morelosgo.data.network.repository

import android.content.Context
import mx.edu.utez.morelosgo.data.network.api.UsuarioAPI
import mx.edu.utez.morelosgo.data.network.dao.UsuarioDao
import mx.edu.utez.morelosgo.data.network.model.Usuario

class UsuarioRepository(context: Context) : UsuarioDao {

    private val api = UsuarioAPI(context)

    override fun create(
        usuario: Usuario,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        api.create(usuario, onSuccess, onError)
    }

    override fun login(
        usuario: Usuario,
        onSuccess: (Usuario) -> Unit,
        onError: (String) -> Unit
    ) {
        api.login(usuario, onSuccess, onError)
    }
}
