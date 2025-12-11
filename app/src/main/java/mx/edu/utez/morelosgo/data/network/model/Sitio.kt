package mx.edu.utez.morelosgo.data.network.model

data class Sitio (
    val idSitio : Int,
    val nombre : String,
    val horarios : String,
    val costos : String,
    val fotografia : String,
    val resenas : String?,
    val tipo : String,
    val latitud : Double,
    val longitud : Double
)