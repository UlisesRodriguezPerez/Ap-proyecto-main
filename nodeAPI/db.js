const Pool = require('pg').Pool;

const pool = new Pool({
    user: "xx",
    password: "xxxxx",
    database: "School_Management",
    host: "x.com",
    port: 19826
})

module.exports = pool;
