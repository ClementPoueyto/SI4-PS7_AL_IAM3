const presence = require('./models/passage-bus-sensor-model.js')
const ID= "T64YJ";
const CONFIG=require('./models/config.js'); 
const SPEED_READ_VALUE= CONFIG.time_speed_simulation;
const VALUES_PRESENCE=[0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,1,0,0];
// 1 = un bus est passé
// 0 = Le bus n'est pas encore passé
presence(CONFIG.port_server_shelter_1,ID,VALUES_PRESENCE,SPEED_READ_VALUE);