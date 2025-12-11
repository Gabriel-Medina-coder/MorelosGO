package mx.edu.utez.morelosgo.utils

import android.content.Context
import android.content.SharedPreferences
import mx.edu.utez.morelosgo.data.network.model.Usuario

object SessionManager {
    private const val PREF_NAME = "MorelosGO_Session"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_NOMBRE = "nombre"
    private const val KEY_CORREO = "correo"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    
    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    
    fun saveUserSession(context: Context, usuario: Usuario) {
        val editor = getPreferences(context).edit()
        editor.putInt(KEY_USER_ID, usuario.idUsuario)
        editor.putString(KEY_NOMBRE, usuario.nombre)
        editor.putString(KEY_CORREO, usuario.correo)
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.apply()
    }
    
    fun getUserId(context: Context): Int {
        return getPreferences(context).getInt(KEY_USER_ID, 0)
    }
    
    fun getNombre(context: Context): String {
        return getPreferences(context).getString(KEY_NOMBRE, "") ?: ""
    }
    
    fun getCorreo(context: Context): String {
        return getPreferences(context).getString(KEY_CORREO, "") ?: ""
    }
    
    fun isLoggedIn(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    fun clearSession(context: Context) {
        val editor = getPreferences(context).edit()
        editor.clear()
        editor.apply()
    }
    
    fun getCurrentUser(context: Context): Usuario? {
        if (!isLoggedIn(context)) return null
        
        val userId = getUserId(context)
        val nombre = getNombre(context)
        val correo = getCorreo(context)
        
        if (userId == 0) return null
        
        return Usuario(
            idUsuario = userId,
            nombre = nombre,
            correo = correo,
            contra = "" // No guardamos la contraseña en sesión por seguridad
        )
    }
}
