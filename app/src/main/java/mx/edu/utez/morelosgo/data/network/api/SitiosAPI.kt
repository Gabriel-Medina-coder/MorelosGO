package mx.edu.utez.morelosgo.data.network.api

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import mx.edu.utez.morelosgo.data.network.VolleySingleton
import mx.edu.utez.morelosgo.data.network.model.Sitio
import org.json.JSONArray
import org.json.JSONObject

class SitiosAPI(private val context: Context) {

    private val baseURL = "http://10.0.2.2:3000" // Emulador Android

    fun getAll(
        onSuccess: (List<Sitio>) -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "$baseURL/sitio"
        val metodo = Request.Method.GET

        val listener = Response.Listener<JSONArray> { response ->
            val lista = mutableListOf<Sitio>()
            for (i in 0 until response.length()) {
                val obj = response.getJSONObject(i)
                lista.add(
                    Sitio(
                        idSitio = obj.getInt("idSitio"),
                        nombre = obj.getString("nombre"),
                        horarios = obj.getString("horarios"),
                        costos = obj.getString("costos"),
                        fotografia = obj.getString("fotografia"),
                        resenas = obj.optString("resenas", null),
                        tipo = obj.getString("tipo"),
                        latitud = obj.getDouble("latitud"),
                        longitud = obj.getDouble("longitud")
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
        sitio: Sitio,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "$baseURL/sitio"
        val metodo = Request.Method.POST

        val body = JSONObject().apply {
            put("idSitio", sitio.idSitio)
            put("nombre", sitio.nombre)
            put("horarios", sitio.horarios)
            put("costos", sitio.costos)
            put("fotografia", sitio.fotografia)
            put("resenas", sitio.resenas ?: "")
            put("tipo", sitio.tipo)
            put("latitud", sitio.latitud)
            put("longitud", sitio.longitud)
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
        sitio: Sitio,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "$baseURL/sitio"
        val metodo = Request.Method.PUT

        val body = JSONObject().apply {
            put("idSitio", sitio.idSitio)
            put("nombre", sitio.nombre)
            put("horarios", sitio.horarios)
            put("costos", sitio.costos)
            put("fotografia", sitio.fotografia)
            put("resenas", sitio.resenas ?: "")
            put("tipo", sitio.tipo)
            put("latitud", sitio.latitud)
            put("longitud", sitio.longitud)
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
        idSitio: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val url = "$baseURL/sitio/$idSitio"
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
