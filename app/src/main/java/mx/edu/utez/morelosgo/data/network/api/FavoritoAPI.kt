package mx.edu.utez.morelosgo.data.network.api

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import mx.edu.utez.morelosgo.data.network.VolleySingleton
import mx.edu.utez.morelosgo.data.network.model.Favorito
import org.json.JSONArray
import org.json.JSONObject

class FavoritosAPI(private val context: Context) {

    private val baseURL = "http://10.0.2.2:3000" // Emulador Android

    fun getAll(
        onSuccess: (List<Favorito>) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "$baseURL/favorito"
        val metodo = Request.Method.GET

        val listener = Response.Listener<JSONArray> { response ->
            val lista = mutableListOf<Favorito>()
            for (i in 0 until response.length()) {
                val obj = response.getJSONObject(i)
                lista.add(
                    Favorito(
                        idFavorito = obj.getInt("idFavorito"),
                        idUsuario = obj.getInt("idUsuario"),
                        idSitio = obj.getInt("idSitio")
                    )
                )
            }
            onSuccess(lista)
        }

        val errorListener = Response.ErrorListener { error ->
            onError(error.message.toString())
        }

        val request = JsonArrayRequest(metodo, url, null, listener, errorListener)
        VolleySingleton.getInstance(context).add(request)
    }

    fun create(
        favorito: Favorito,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "$baseURL/favorito"
        val metodo = Request.Method.POST

        val body = JSONObject().apply {
            put("idFavorito", favorito.idFavorito)
            put("idUsuario", favorito.idUsuario)
            put("idSitio", favorito.idSitio)
        }

        val listener = Response.Listener<JSONObject> { response ->
            if (response.optInt("affectedRows") == 1) onSuccess()
        }

        val errorListener = Response.ErrorListener { error ->
            onError(error.message.toString())
        }

        val request = JsonObjectRequest(metodo, url, body, listener, errorListener)
        VolleySingleton.getInstance(context).add(request)
    }

    fun update(
        favorito: Favorito,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "$baseURL/favorito"
        val metodo = Request.Method.PUT

        val body = JSONObject().apply {
            put("idFavorito", favorito.idFavorito)
            put("idUsuario", favorito.idUsuario)
            put("idSitio", favorito.idSitio)
        }

        val listener = Response.Listener<JSONObject> { response ->
            if (response.optInt("affectedRows") == 1) onSuccess()
        }

        val errorListener = Response.ErrorListener { error ->
            onError(error.message.toString())
        }

        val request = JsonObjectRequest(metodo, url, body, listener, errorListener)
        VolleySingleton.getInstance(context).add(request)
    }

    fun delete(
        idFavorito: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "$baseURL/favorito/$idFavorito"
        val metodo = Request.Method.DELETE

        val listener = Response.Listener<JSONObject> { response ->
            if (response.optInt("affectedRows") == 1) onSuccess()
        }

        val errorListener = Response.ErrorListener { error ->
            onError(error.message.toString())
        }

        val request = JsonObjectRequest(metodo, url, null, listener, errorListener)
        VolleySingleton.getInstance(context).add(request)
    }
}
