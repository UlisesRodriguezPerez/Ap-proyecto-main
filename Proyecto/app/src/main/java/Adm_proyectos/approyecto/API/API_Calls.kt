package Adm_proyectos.approyecto.API

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*


interface API_Calls {
    @GET("usuarios/{id}")
    suspend fun getUsusario(@Path("id") id:String): Response<ArrayList<JsonObject>>

    @GET("usuarios")
    suspend fun getUsusarios(): Response<ArrayList<JsonObject>>

    @GET("logIn/{correo}")
    suspend fun getLogin(@Path("correo") correo:String): Response<ArrayList<JsonObject>>

    @GET("tipoUsuario/{IdUsu}")
    suspend fun getTipoUsuario(@Path("IdUsu") idUsuario:String): Response<ArrayList<JsonObject>>

    @GET("cursos")
    suspend fun getCursosAdmin(): Response<ArrayList<JsonObject>>

    @GET("cursos/estudiante/{cedula}")
    suspend fun getCursosEstudiante(@Path("cedula") cedula:String): Response<ArrayList<JsonObject>>

    @GET("cursos/profesor/{profCorreo}")
    suspend fun getCursosProfesor(@Path("profCorreo") correo:String): Response<ArrayList<JsonObject>>

    @GET("cursos/info/{cod}/{clase}")
    suspend fun getCursoInfo(@Path("cod") codigoCurso:String, @Path("clase") gradoCurso: String): Response<ArrayList<JsonObject>>

    @GET("profesores")
    suspend fun getProfesores(): Response<ArrayList<JsonObject>>

    @GET("gradoId/{numGrad}")
    suspend fun getCursoInfo(@Path("numGrad") codigoCurso:String): Response<ArrayList<JsonObject>>

    @GET("profesores/{ced}")
    suspend fun getInfoProfesor(@Path("ced") cedula:String): Response<ArrayList<JsonObject>>

    @GET("estudiantes")
    suspend fun getEstudiantes(): Response<ArrayList<JsonObject>>

    @GET("estudiantes/{nom}/{apel}")
    suspend fun getInfoEstudiante(@Path("nom") nombre:String, @Path("apel") apellido:String): Response<ArrayList<JsonObject>>

//    @POST("/nuevoCurso")
//    suspend fun insertarCurso(@Query("codigo") codigo : String,
//                              @Query("nombre") nombre:String,
//                              @Query("gradoId") gradoId:String,
//                              @Query("diaSemana") diaSemana:String,
//                              @Query("horaInicio") horaInicio:String,
//                              @Query("horaFin") horaFin:String, ) : Response<Int>

    @GET("nuevoCurso/{codigo}/{nombre}/{gradoId}/{diaSemana}/{horaInicio}/{horaFin}")
    suspend fun insertarCurso(@Path("codigo") codigo : String,
                              @Path("nombre") nombre:String,
                              @Path("gradoId") gradoId:String,
                              @Path("diaSemana") diaSemana:String,
                              @Path("horaInicio") horaInicio:String,
                              @Path("horaFin") horaFin:String) : Response<ArrayList<JsonObject>>

    @GET("nuevoDocente/{cedula}/{nombre}/{correo}/{contra}/{apellido}")
    suspend fun insertarProfesor(@Path("cedula") cedula : String,
                                  @Path("nombre") nombre:String,
                                  @Path("correo") correo:String,
                                  @Path("contra") contra:String,
                                  @Path("apellido") apellido:String) : Response<ArrayList<JsonObject>>

    @GET("nuevoAlumno/{cedula}/{nombre}/{correo}/{contra}/{apellido}/{grado}")
    suspend fun insertarEstudiante(@Path("cedula") cedula : String,
                                     @Path("nombre") nombre:String,
                                     @Path("correo") correo:String,
                                     @Path("contra") contra:String,
                                     @Path("apellido") apellido:String,
                                     @Path("grado") grado:String) : Response<ArrayList<JsonObject>>

//    @PUT("updateCurso")
//    suspend fun updateCurso(@Query("codviejo") codviejo:String,
//                            @Query("gradviejo") gradviejo:String,
//                            @Query("codigo") codigo : String,
//                            @Query("nombre") nombre:String,
//                            @Query("gradoId") gradoId:String,
//                            @Query("diaSemana") diaSemana:String,
//                            @Query("horaInicio") horaInicio:String,
//                            @Query("horaFin") horaFin:String, ) : Response<ArrayList<JsonObject>>

    @GET("updateCurso/{codviejo}/{gradviejo}/{nombre}/{codigo}/{diaSemana}/{horaInicio}/{horaFin}/{gradoId}")
    suspend fun updateCurso(@Path("codviejo") codviejo:String,
                            @Path("gradviejo") gradviejo:String,
                            @Path("codigo") codigo : String,
                            @Path("nombre") nombre:String,
                            @Path("gradoId") gradoId:String,
                            @Path("diaSemana") diaSemana:String,
                            @Path("horaInicio") horaInicio:String,
                            @Path("horaFin") horaFin:String, ) : Response<ArrayList<JsonObject>>

    @GET("updateDocente/{cedvieja}/{cedula}/{nombre}/{correo}/{contra}/{apellido}")
    suspend fun updateProfesor( @Path("cedvieja") cedvieja : String,
                                @Path("cedula") cedula : String,
                                 @Path("nombre") nombre:String,
                                 @Path("correo") correo:String,
                                 @Path("contra") contra:String,
                                 @Path("apellido") apellido:String) : Response<ArrayList<JsonObject>>


    @GET("updateAlumno/{nombviejo}/{apeviejo}/{cedula}/{nombre}/{correo}/{contra}/{apellido}/{grado}")
    suspend fun updateEstudiante(@Path("nombviejo") cedvieja : String,
                                 @Path("apeviejo") apeviejo : String,
                                 @Path("cedula") cedula : String,
                                   @Path("nombre") nombre:String,
                                   @Path("correo") correo:String,
                                   @Path("contra") contra:String,
                                   @Path("apellido") apellido:String,
                                   @Path("grado") grado:String) : Response<ArrayList<JsonObject>>

    @GET("asignarProfe/{cedula}/{codigo}/{grado}")
    suspend fun asignarProfesor(@Path("cedula") cedula : String,
                                 @Path("codigo") codigo:String,
                                 @Path("grado") grado:String) : Response<ArrayList<JsonObject>>

    @GET("asignarAlumno/{nombre}/{apellido}/{codigo}/{grado}")
    suspend fun asignarEstudiante(@Path("nombre") nombre : String,
                                @Path("apellido") apellido: String,
                                @Path("codigo") codigo: String,
                                @Path("grado") grado: String) : Response<ArrayList<JsonObject>>

    @GET("estudiantesCed/{ced}")
    suspend fun getBuscarEstudiante(@Path("ced") cedula:String): Response<ArrayList<JsonObject>>

    @GET("nuevaTarea/{codigo}/{clase}/{codtarea}/{titulo}/{contenido}/{fecha}")
    suspend fun insertarTarea(@Path("codigo") codigo : String,
                              @Path("clase") clase:String,
                              @Path("codtarea") codtarea:String,
                              @Path("titulo") titulo:String,
                              @Path("contenido") contenido:String,
                              @Path("fecha") fecha:String) : Response<ArrayList<JsonObject>>

    @GET("nuevaNoticia/{codigo}/{clase}/{titulo}/{contenido}/{fecha}")
    suspend fun insertarNoticia(@Path("codigo") codigo : String,
                              @Path("clase") clase:String,
                              @Path("titulo") titulo:String,
                              @Path("contenido") contenido:String,
                              @Path("fecha") fecha:String) : Response<ArrayList<JsonObject>>

    @GET("noticias/{codigo}/{clase}")
    suspend fun getNoticias(@Path("codigo") codigo:String, @Path("clase") clase:String): Response<ArrayList<JsonObject>>

    @GET("tareas/{codigo}/{clase}")
    suspend fun getTareas(@Path("codigo") codigo:String, @Path("clase") clase:String): Response<ArrayList<JsonObject>>

    @GET("chat/{codigo}/{clase}")
    suspend fun getChat(@Path("codigo") codigo:String, @Path("clase") clase:String): Response<ArrayList<JsonObject>>

    @GET("publicaMsg/{curso}/{grado}/{correo}/{mensaje}")
    suspend fun insertarMensaje(@Path("curso") curso : String,
                                @Path("grado") grado:String,
                                @Path("correo") correo:String,
                                @Path("mensaje") mensaje:String) : Response<ArrayList<JsonObject>>

    @GET("elimCurso/{cod}/{grad}")
    suspend fun eliminarCurso(@Path("cod") codigo:String, @Path("grad") grado:String): Response<ArrayList<JsonObject>>

    @GET("elimDocente/{cedula}")
    suspend fun eliminarProfesor(@Path("cedula") cedula:String): Response<ArrayList<JsonObject>>

    @GET("elimAlumno/{cedula}")
    suspend fun eliminarAlumno(@Path("cedula") cedula:String): Response<ArrayList<JsonObject>>

    @GET("profesoresCur/{codigo}/{clase}")
    suspend fun profesoresPorCurso(@Path("codigo") codigo:String, @Path("clase") clase:String): Response<ArrayList<JsonObject>>

    @GET("estudiantesCur/{codigo}/{clase}")
    suspend fun estudiantesPorCurso(@Path("codigo") codigo:String, @Path("clase") clase:String): Response<ArrayList<JsonObject>>

    @GET("votarNota/{cedula}/{nuevanota}")
    suspend fun actualizarNotaProfesor(@Path("cedula") cedula:String, @Path("nuevanota") nuevaNota:String): Response<ArrayList<JsonObject>>

    @GET("CedPorCurso/{cod}/{grad}")
    suspend fun cedulaProfesorPorCurso(@Path("cod") codigo:String, @Path("grad") grado:String): Response<ArrayList<JsonObject>>





}