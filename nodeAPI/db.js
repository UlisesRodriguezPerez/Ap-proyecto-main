const Pool = require('pg').Pool;

const pool = new Pool({
    user: "Ferreto",
    password: "Famferre01@",
    database: "School_Management",
    host: "postgresql-46448-0.cloudclusters.net",
    port: 19826
})

module.exports = pool;