const infrared = require('./models/infra-red-light-route-model.js')
const ID= "T64YJ"
const CONFIG=require('./models/config.js'); 
const LOG_ERROR=true;
const SPEED_READ_VALUE= CONFIG.time_speed_simulation;
MAX_TIME_BEFORE_TRAFFIC_JAM=5;// Depassing this time (in seconds) , there s a traffic jam
const VALUES_INFRA_RED_LIGHT=[0,0,1,1,1,1,1,1,0,0,1,1,1,1,1,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,0,0,0,0,1,1,0,0]
// 1 = Detecte une voiture
// 0 = Ne détecte pas de voiture

var infrared_function=new infrared(CONFIG.port_server_shelter_1,
    ID,
    VALUES_INFRA_RED_LIGHT,
    MAX_TIME_BEFORE_TRAFFIC_JAM,
    SPEED_READ_VALUE,
    LOG_ERROR);
infrared_function.start();