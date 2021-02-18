const server = require('./models/server-model.js')
const MODE_TRANSPORT='BUS';
const CONFIG=require('./models/config.js'); 
const LOG_ERROR=true;
var serv= new server(MODE_TRANSPORT,CONFIG.port_server_shelter_1,CONFIG,LOG_ERROR);
serv.start();
