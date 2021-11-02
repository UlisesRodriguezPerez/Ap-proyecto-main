var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var pg = require('pg');
const pool = require("./db")
let port = process.env.port || 3000;

app.use(express.json())


 app.get('/', function(req, res){
    res.send('Hola Putitos!');
}); 

app.get("/usuarios", async(req, res)=>{
    try{
        const usuarios = await pool.query("SELECT * FROM usuario");
        res.json(usuarios.rows)

    }catch(err) {
        console.error(err.message);

    }

});

app.get("/usuarios/:id", async(req, res)=>{
    const { id } = req.params
    try{
        const usuarios = await pool.query("SELECT * FROM usuario WHERE \"ID\" = $1", [id]);
        res.json(usuarios.rows)

    }catch(err) {
        console.error(err.message);

    }

});

app.listen(5000);
console.log('API running on port 5000')