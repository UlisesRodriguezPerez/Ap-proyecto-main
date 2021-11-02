package Adm_proyectos.approyecto.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroInstance {

    private val retrofit by lazy{
        Retrofit.Builder()
            .baseUrl("http://nodejsclusters-47901-0.cloudclusters.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: API_Calls by lazy{
        retrofit.create(API_Calls::class.java)
    }

}