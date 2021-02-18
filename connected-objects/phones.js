const phones = require('./models/phones-model.js')
const CONFIG=require('./models/config.js'); 
const SPEED_READ_VALUE= CONFIG.time_speed_simulation;
const VALUES_CONNEXIONS=[0,0,1,1,1,1,0,0,0,0,1,1,2,0,2,0,2,2,0,0,0,0,0,0,2,0,0,0,0,0,0,0,0];
// 1 = Une personne est dans l'abri de bus
// 0 = Rien ne se passe
// 2 = Une perrsonne quitte l'abri de bus
phones(CONFIG.port_server_shelter_1,VALUES_CONNEXIONS,SPEED_READ_VALUE);