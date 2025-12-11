package mx.edu.utez.morelosgo.data.network.api

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import mx.edu.utez.morelosgo.data.network.VolleySingleton
import mx.edu.utez.morelosgo.data.network.model.Usuario
import org.json.JSONArray
import org.json.JSONObject

class UsuarioAPI(private val context : Context) {
    val baseURL = "http://10.0.2.2:3000" //Para ejecutar en el emulador

    fun create(
        usuario: Usuario,
        onSuccess : () -> Unit,
        onError : (String) -> Unit
    ){
        val url = "$baseURL/usuario"
        val metodo = Request.Method.POST
        val body = JSONObject()
        body.put("idUsuario", usuario.idUsuario)
        body.put("nombre", usuario.nombre)
        body.put("contra", usuario.contra)
        body.put("correo", usuario.correo)
        val listener = Response.Listener<JSONObject>{ response ->
            if(response.getInt("affectedRows") == 1){
                onSuccess()
            }
        }
        val errorrListener = Response.ErrorListener{error ->
            onError(error.message.toString())
        }
        val request = JsonObjectRequest(metodo, url, body, listener, errorrListener)
        VolleySingleton.getInstance(context).add(request)
    }

    fun login(
        usuario: Usuario,
        onSuccess : (Usuario) -> Unit,
        onError : (String) -> Unit
    ){
        val url = "$baseURL/login"
        val metodo = Request.Method.POST
        val body = JSONObject()
        body.put("correo", usuario.correo)
        body.put("contra", usuario.contra)

        val listener = Response.Listener<JSONObject>{ response ->

            onSuccess(
                Usuario(
                    response.getInt("idUsuario"),
                    response.getString("nombre"),
                    response.getString("correo"),
                    response.getString("contra")
                )
            )
        }

        val errorrListener = Response.ErrorListener{ error ->
            onError(error.message.toString())
        }

        val request = JsonObjectRequest(metodo, url, body, listener, errorrListener)
        VolleySingleton.getInstance(context).add(request)
    }

}