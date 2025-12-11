package mx.edu.utez.morelosgo.data.network.dao;

import kotlin.Unit;
import mx.edu.utez.morelosgo.data.network.model.Usuario;

interface UsuarioDao {

    fun create(
        usuario: Usuario,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )

    fun login(
        usuario: Usuario,
        onSuccess: (Usuario) -> Unit,
        onError: (String) -> Unit
    )
}
