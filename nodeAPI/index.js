var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var pg = require('pg');
const pool = require("./db")

app.use(express.json())

const PORT = 8080;
const HOST = '0.0.0.0';

/* app.get('/', function(req, res){
    res.send('Hola Putitos!');
}); */

//Prueba para lista de resultados
app.get("/usuarios", async(req, res)=>{
    try{
        const usuarios = await pool.query("SELECT * FROM usuario");
        res.json(usuarios.rows)

    }catch(err) {
        console.error(err.message);

    }

});

//Prueba resultado especifico
app.get("/usuarios/:id", async(req, res)=>{
    const { id } = req.params
    try{
        const usuarios = await pool.query("SELECT * FROM usuario WHERE \"ID\" = $1", [id]);
        res.json(usuarios.rows)

    }catch(err) {
        console.error(err.message);

    }

});

//Log in, ENTRADAS: El correo del usuario SALIDA: la contraseña del usuario
app.get("/logIn/:correo", async(req, res)=>{
    const { correo } = req.params
    try{
        const contra = await pool.query("SELECT \"ID\",contrasenna, nombre FROM usuario WHERE correo = $1", [correo]);
        res.json(contra.rows)

    }catch(err) {
        console.error(err.message);

    }

});

//Log in, ENTRADAS: El correo del usuario SALIDA: la contraseña del usuario
app.get("/tipoUsuario/:IdUsu", async(req, res)=>{
    const { IdUsu } = req.params
    try{
        const tipo = await pool.query("SELECT * FROM tipousuario($1)", [IdUsu]);
        res.json(tipo.rows)

    }catch(err) {
        console.error(err.message);

    }

});

//Dar todos los cursos a Admin, ENTRADAS: ninguna SALIDA: todos los cursos
app.get("/cursos", async(req, res)=>{
    try{
        const cursos = await pool.query("SELECT curso.codigo, clase, curso.nombre FROM curso INNER JOIN grado ON curso.\"gradoId\" = grado.\"ID\"");
        res.json(cursos.rows)

    }catch(err) {
        console.error(err.message);

    }

});


//Dar todos los cursos a Estudiantes con su cedula, ENTRADAS: la cedula del estudiante SALIDA: los cursos del estudiante con ese correo
app.get("/cursos/estudiante/:cedula", async(req, res)=>{
    const { cedula } = req.params
    try{
        const cursos = await pool.query("SELECT * FROM cursosEstudiante($1)", [cedula]);
        res.json(cursos.rows)

    }catch(err) {
        console.error(err.message);

    }

});

//Dar todos los cursos a Profesores con su correo, ENTRADAS: el correo del profesor SALIDA: los cursos del estudiante con ese correo
app.get("/cursos/profesor/:profCorreo", async(req, res)=>{
    const { profCorreo } = req.params
    try{
        const cursos = await pool.query("SELECT * FROM cursosProfesor($1)", [profCorreo]);
        res.json(cursos.rows)

    }catch(err) {
        console.error(err.message);

    }

});

//Detalles de los cursos segun codigo y clase(Admin), ENTRADAS: El codigo del curso y el grado SALIDA: detalles del curso
app.get("/cursos/info/:cod/:clase", async(req, res)=>{
    const { cod } = req.params
    const { clase } = req.params
    try{
        const cursos 
        = await pool.query("SELECT codigo, nombre, clase, \"horaInicio\", \"horaFin\", \"diaSemana\"FROM curso INNER JOIN grado ON \"gradoId\" = grado.\"ID\" WHERE codigo = $1 AND clase = $2;", [cod,clase]);
        res.json(cursos.rows)

    }catch(err) {
        console.error(err.message);

    }

});


//Lista de todos los profesores, ENTRADAS: ninguna SALIDA:lista de todos los profesores
app.get("/profesores", async(req, res)=>{
    try{
        const usuarios = await pool.query("SELECT usuario.nombre, usuario.apellido, usuario.cedula FROM profesor INNER JOIN usuario ON profesor.\"usuarioId\" = usuario.\"ID\"");
        res.json(usuarios.rows)

    }catch(err) {
        console.error(err.message);

    }

});

//Conseguir ID del un grado, ENTRADAS: el numero del grado(prepa, 1, 2, ...) SALIDA: el ID de BD del grado
app.get("/gradoId/:numGrad", async(req, res)=>{
    const { numGrad } = req.params
    try{
        const grado = await pool.query("SELECT \"ID\" FROM grado WHERE clase = $1", [numGrad]);
        res.json(grado.rows)

    }catch(err) {
        console.error(err.message);

    }

});

//Conseguir detalles de un docente, ENTRADAS: cedula del maestro SALIDA: Detalles del profesor
app.get("/profesores/:ced", async(req, res)=>{
    const { ced } = req.params
    try{
        const profesor = await pool.query("SELECT nombre,contrasenna, cedula, apellido, correo, calificacion FROM profesor INNER JOIN usuario ON profesor.\"usuarioId\" = usuario.\"ID\" WHERE cedula = $1" , [ced]);
        res.json(profesor.rows)

    }catch(err) {
        console.error(err.message);

    }

});


//Lista de todos los alumnos, ENTRADAS: nada SALIDA: Lista de todos los estudiantes
app.get("/estudiantes", async(req, res)=>{
    try{
        const estudiantes = await pool.query("SELECT usuario.nombre, usuario.apellido, usuario.nombre, grado.clase FROM estudiante INNER JOIN usuario ON estudiante.\"usuarioId\" = usuario.\"ID\" INNER JOIN grado ON estudiante.\"gradoId\" = grado.\"ID\"");
        res.json(estudiantes.rows)

    }catch(err) {
        console.error(err.message);

    }

});


//Conseguir detalles de un alumno, ENTRADAS: nombre y apellido del alumno SALIDA: detalles del alumno
app.get("/estudiantes/:nom/:apel", async(req, res)=>{
    const { nom } = req.params
    const { apel } = req.params
    try{
        const estudiante = await pool.query("SELECT nombre, cedula, contrasenna, apellido, clase FROM estudiante INNER JOIN usuario ON estudiante.\"usuarioId\" = usuario.\"ID\" INNER JOIN grado ON estudiante.\"gradoId\" = grado.\"ID\" WHERE nombre = $1 AND apellido = $2", [nom, apel]);
        res.json(estudiante.rows)

    }catch(err) {
        console.error(err.message);

    }

})

//Lista de estudiantes en una clase, ENTRADAS: clase y codigo del curso SALIDA: Lista de estudiantes
app.get("/estudiantesCur/:codigo/:clase", async(req, res)=>{
    const { codigo } = req.params
    const { clase } = req.params
    try{
        const alumno = await pool.query("SELECT usuario.nombre, apellido, cedula FROM \"cursoXestudiante\" INNER JOIN estudiante ON estudiante.\"ID\" = \"estudianteId\" INNER JOIN usuario ON usuario.\"ID\"= \"usuarioId\" INNER JOIN curso ON curso.\"ID\" = \"cursoId\" INNER JOIN grado ON grado.\"ID\"= curso.\"gradoId\" WHERE curso.codigo = $1 AND grado.clase = $2", [codigo, clase]);
        res.json(alumno.rows)

    }catch(err) {
        console.error(err.message);

    }

})

//Lista de profesores en una clase, ENTRADAS: clase y codigo del curso SALIDA: Lista de profesores
app.get("/profesoresCur/:codigo/:clase", async(req, res)=>{
    const { codigo } = req.params
    const { clase } = req.params
    try{
        const profesor = await pool.query("SELECT usuario.nombre, apellido, cedula, calificacion, correo FROM \"cursoXprofesor\" INNER JOIN profesor ON profesor.\"ID\" = \"profesorId\" INNER JOIN usuario ON usuario.\"ID\"= \"usuarioId\" INNER JOIN curso ON curso.\"ID\" = \"cursoId\" INNER JOIN grado ON grado.\"ID\"= curso.\"gradoId\" WHERE curso.codigo = $1 AND grado.clase = $2", [codigo, clase]);
        res.json(profesor.rows)

    }catch(err) {
        console.error(err.message);

    }

})

//Inserta nuevo curso, ENTRADAS: se ven abajo SALIDA: numero de resultado(0 si salio bien)
app.get("/nuevoCurso/:codigo/:nombre/:gradoId/:diaSemana/:horaInicio/:horaFin", async(req, res)=>{
    
    try{
        const { codigo } = req.params;
        const { nombre } = req.params;
        const { gradoId } = req.params;
        const { diaSemana } = req.params;
        const { horaInicio } = req.params;
        const { horaFin } = req.params;
        const newCurso = await pool.query("SELECT * FROM insertarcurso($1, $2, $3, $4, $5, $6)", [codigo, nombre, gradoId, diaSemana, horaInicio, horaFin]);
        res.json(newCurso.rows)

    }catch(err) {
        console.error(err.message);

    }

});

//Inserta nuevo profesor, ENTRADA:se ven abajo SALIDA:numero de resultado(0 si salio bien):
app.get("/nuevoDocente/:cedula/:nombre/:correo/:contra/:apellido", async(req, res)=>{
    
    try{
        const { cedula } = req.params;
        const { nombre } = req.params;
        const { correo } = req.params;
        const { contra } = req.params;
        const { apellido } = req.params;
        const newProfe = await pool.query("SELECT * FROM insertardocente($1, $2, $3, $4, $5)", [cedula,nombre,correo,contra,apellido]);
        res.json(newProfe.rows)

    }catch(err) {
        console.error(err.message);

    }

});


//Inserta nuevo estudiante, ENTRADAS: se ven abajo SALIDA: numero de resultado(0 si salio bien)
app.get("/nuevoAlumno/:cedula/:nombre/:correo/:contra/:apellido/:grado", async(req, res)=>{
    
    try{
        const { cedula } = req.params;
        const { nombre } = req.params;
        const { correo } = req.params;
        const { contra } = req.params;
        const { apellido } = req.params;
        const { grado } = req.params;
        const newAlumno = await pool.query("SELECT * FROM insertaralumno($1, $2, $3, $4, $5, $6)", [cedula,nombre,correo,contra,apellido,grado]);
        res.json(newAlumno.rows)

    }catch(err) {
        console.error(err.message);

    }

});

//Actualiza un curso , ENTRADAS: se ven abajo SALIDA: numero de resultado(0 si salio bien)
app.get("/updateCurso/:codviejo/:gradviejo/:nombre/:codigo/:diaSemana/:horaInicio/:horaFin/:gradoId", async(req, res)=>{
    
    try{
        const { codviejo } = req.params;
        const { gradviejo } = req.params;
        const { codigo } = req.params;
        const { nombre } = req.params;
        const { gradoId } = req.params;
        const { diaSemana } = req.params;
        const { horaInicio } = req.params;
        const { horaFin } = req.params;
        const newCurso = await pool.query("SELECT * FROM actualizarcurso($1, $2, $3, $4, $5, $6, $7, $8)", [codviejo, gradviejo, nombre, codigo, horaInicio, horaFin, diaSemana, gradoId]);
        res.json(newCurso.rows)

    }catch(err) {
        console.error(err.message);

    }

});


//Actualiza un maestro , ENTRADAS: se ven abajo SALIDA: numero de resultado(0 si salio bien)
app.get("/updateDocente/:cedvieja/:cedula/:nombre/:correo/:contra/:apellido", async(req, res)=>{
    
    try{
        const { cedvieja } = req.params;
        const { cedula } = req.params;
        const { nombre } = req.params;
        const { correo } = req.params;
        const { contra } = req.params;
        const { apellido } = req.params;
        const newProfe = await pool.query("SELECT * FROM actualizardocente($1, $2, $3, $4, $5, $6)", [cedvieja, cedula,nombre,correo,contra,apellido]);
        res.json(newProfe.rows)

    }catch(err) {
        console.error(err.message);

    }

});


//Actualiza un estudiante , ENTRADAS: se ven abajo SALIDA: numero de resultado(0 si salio bien)
app.get("/updateAlumno/:nombviejo/:apeviejo/:cedula/:nombre/:correo/:contra/:apellido/:grado", async(req, res)=>{
    
    try{
        const { nombviejo } = req.params;
        const { apeviejo } = req.params;
        const { cedula } = req.params;
        const { nombre } = req.params;
        const { correo } = req.params;
        const { contra } = req.params;
        const { apellido } = req.params;
        const { grado } = req.params;
        const newAlumno = await pool.query("SELECT * FROM actualizaralumno($1, $2, $3, $4, $5, $6, $7, $8)", [nombviejo,apeviejo,cedula,nombre,correo,contra,apellido,grado]);
        res.json(newAlumno.rows)

    }catch(err) {
        console.error(err.message);

    }
});


app.get("/elimCurso/:cod/:grad", async(req, res)=>{
    
    try{
        const { cod } = req.params;
        const { grad } = req.params;
        const curso = await pool.query("SELECT * FROM eliminarcurso($1, $2)", [cod, grad]);
        res.json(curso.rows)

    }catch(err) {
        console.error(err.message);

    }

});

app.get("/elimDocente/:cedula", async(req, res)=>{
    
    try{
        const { cedula } = req.params;
        const profe = await pool.query("SELECT * FROM eliminardocente($1)", [cedula]);
        res.json(profe.rows)

    }catch(err) {
        console.error(err.message);

    }

});

app.get("/elimAlumno/:cedula", async(req, res)=>{
    
    try{
        const { cedula } = req.params;
        const profe = await pool.query("SELECT * FROM eliminaralumno($1)", [cedula]);
        res.json(profe.rows)

    }catch(err) {
        console.error(err.message);

    }

});

//Asigna un profesor a un curso , ENTRADAS: se ven abajo SALIDA: numero de resultado(0 si salio bien)
app.get("/asignarProfe/:cedula/:codigo/:grado", async(req, res)=>{
    
    try{
        const { cedula } = req.params;
        const { codigo } = req.params;
        const { grado } = req.params;
        const asignacion = await pool.query("SELECT * FROM asignarprofe($1, $2, $3)", [cedula,codigo,grado]);
        res.json(asignacion.rows)

    }catch(err) {
        console.error(err.message);

    }
});


//Asigna un estudiante a un curso , ENTRADAS: se ven abajo SALIDA: numero de resultado(0 si salio bien)
app.get("/asignarAlumno/:nombre/:apellido/:codigo/:grado", async(req, res)=>{
    
    try{
        const { nombre } = req.params;
        const { apellido } = req.params;
        const { codigo } = req.params;
        const { grado } = req.params;
        const asignacion = await pool.query("SELECT * FROM asignaralumno($1, $2, $3, $4)", [nombre,apellido,codigo,grado]);
        res.json(asignacion.rows)

    }catch(err) {
        console.error(err.message);

    }
});


//Conseguir detalles de un alumno, ENTRADAS: cedula SALIDA: detalles del alumno
app.get("/estudiantesCed/:ced", async(req, res)=>{
    const { ced } = req.params
    try{
        const estudiante = await pool.query("SELECT nombre, cedula, apellido, clase FROM estudiante INNER JOIN usuario ON estudiante.\"usuarioId\" = usuario.\"ID\" INNER JOIN grado ON estudiante.\"gradoId\" = grado.\"ID\" WHERE cedula = $1", [ced]);
        res.json(estudiante.rows)

    }catch(err) {
        console.error(err.message);

    }

})


//Crea una nueva tarea para un curso, ENTRADAS: Se ven abajo SALIDA: numero de resultado(0 si salio bien)
app.get("/nuevaTarea/:codigo/:clase/:codtarea/:titulo/:contenido/:fecha", async(req, res)=>{
    const { codigo } = req.params
    const { clase } = req.params
    const { codtarea } = req.params
    const { titulo } = req.params
    const { contenido } = req.params
    const { fecha } = req.params
    try{
        const tarea = await pool.query("SELECT * FROM insertartarea($1, $2, $3, $4, $5, $6)", [codigo, clase, titulo, contenido, codtarea, fecha]);
        res.json(tarea.rows)

    }catch(err) {
        console.error(err.message);

    }

})


//Crea una nueva noticia para un curso, ENTRADAS: Se ven abajo SALIDA: numero de resultado(0 si salio bien)
app.get("/nuevaNoticia/:codigo/:clase/:titulo/:contenido/:fecha", async(req, res)=>{
    const { codigo } = req.params
    const { clase } = req.params
    const { titulo } = req.params
    const { contenido } = req.params
    const { fecha } = req.params
    try{
        const tarea = await pool.query("SELECT * FROM insertarnoticia($1, $2, $3, $4, $5)", [codigo, clase, titulo, contenido, fecha]);
        res.json(tarea.rows)

    }catch(err) {
        console.error(err.message);

    }

})


//Lista de noticias de un curso, ENTRADAS: codigo y grado del curso SALIDA: Lista de noticias
app.get("/noticias/:codigo/:clase", async(req, res)=>{
    const { codigo } = req.params
    const { clase } = req.params
    try{
        const noticia = await pool.query("SELECT * FROM noticia INNER JOIN curso ON curso.\"ID\" = \"cursoId\" INNER JOIN grado ON grado.\"ID\" = \"gradoId\" WHERE codigo = $1 AND clase = $2", [codigo, clase]);
        res.json(noticia.rows)

    }catch(err) {
        console.error(err.message);

    }

})


//Lista de tareas de un curso, ENTRADAS: codigo y grado del curso SALIDA: Lista de tareas
app.get("/tareas/:codigo/:clase", async(req, res)=>{
    const { codigo } = req.params
    const { clase } = req.params
    try{
        const tarea = await pool.query("SELECT * FROM tarea INNER JOIN curso ON curso.\"ID\" = \"cursoId\" INNER JOIN grado ON grado.\"ID\" = \"gradoId\" WHERE codigo = $1 AND clase = $2", [codigo, clase]);
        res.json(tarea.rows)

    }catch(err) {
        console.error(err.message);

    }

})

//Lista de mensajes del chat de un curso, ENTRADAS: codigo y grado del curso SALIDA: Lista de mensajes
app.get("/chat/:codigo/:clase", async(req, res)=>{
    const { codigo } = req.params
    const { clase } = req.params
    try{
        const mensajes = await pool.query("SELECT usuario.nombre, apellido, texto FROM mensaje INNER JOIN chat ON chat.\"ID\" = \"chatId\" INNER JOIN curso ON curso.\"ID\" = \"cursoId\" INNER JOIN grado ON grado.\"ID\" = curso.\"gradoId\" INNER JOIN usuario ON usuario.\"ID\" = mensaje.\"usuarioId\" WHERE codigo = $1 AND clase = $2", [codigo, clase]);
        res.json(mensajes.rows)

    }catch(err) {
        console.error(err.message);

    }

})


//Lista de mensajes del chat de un curso, ENTRADAS: ID del chat y del escritor ademas del mensaje SALIDA: numero de resultado(0 si salio bien)
app.get("/publicaMsg/:chat/:usuario/:mensaje", async(req, res)=>{
    const { chat } = req.params
    const { usuario } = req.params
    const { mensaje } = req.params
    try{
        const mensajes = await pool.query("SELECT * FROM insertarMensaje($1, $2, $3)", [chat, usuario, mensaje]);
        res.json(mensajes.rows)

    }catch(err) {
        console.error(err.message);

    }

})



app.listen(PORT, HOST);
console.log('API running on port 8080')