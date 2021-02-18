const presence = require('./models/visits-bus-sensor-model.js')
const ID= "T64YJ";
const CONFIG=require('./models/config.js'); 
const SPEED_READ_VALUE= CONFIG.time_speed_simulation;
//Nombre de personnes qui rentre dans le bus à un instant t
const VALUES_PRESENCE_ENTER=[0,5,5,4,5,0,0,0,0,0,0,0,0,1,0,0,0];
//Nombre de personnes qui quitte le bus à un instant t
const VALUES_PRESENCE_OUT=  [0,0,0,0,1,0,10,0,0,3,5,0,0,0,0,0,1];
//Maximum de personnes dans le transport
const MAX_PEOPLE=CONFIG.max_people;
presence(
    CONFIG.port_server_shelter_1,
    CONFIG.port_server_visits_bus_sensor,
    ID,
    VALUES_PRESENCE_ENTER,VALUES_PRESENCE_OUT,
    MAX_PEOPLE,
    SPEED_READ_VALUE);