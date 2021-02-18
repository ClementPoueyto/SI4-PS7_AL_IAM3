const infrared = require('../models/infra-red-light-route-model.js')
const ID= "T64YJ"
const CONFIG=require('../models/config.js'); 
const SPEED_READ_VALUE= CONFIG.time_speed_simulation;
MAX_TIME_BEFORE_TRAFFIC_JAM=5;// Depassing this time (in seconds) , there s a traffic jam
const VALUES_INFRA_RED_LIGHT=[0,0,1,1,1,1,1,1,0,0,1,1,1,1,1,0,0,0,0,0,1,1,1,1,1,1,1,0,0,0,0,0,0,0,1,1,0,0]
// 1 = Detecte une voiture
// 0 = Ne détecte pas de voiture
const request = require('supertest');
const assert = require('assert');

var infrared_function=new infrared(CONFIG.port_server_shelter_1,
    ID,
    VALUES_INFRA_RED_LIGHT,
    MAX_TIME_BEFORE_TRAFFIC_JAM,
    SPEED_READ_VALUE);


describe("Test infra-red light sensor behaviour", function() {
    
    describe('#lookfor_traffic_jam() : Looking for traffic jam', function () {
        
        before(function(done){
            this.timeout((MAX_TIME_BEFORE_TRAFFIC_JAM+5)*1000);
            infrared_function.last_time_car_detected=Date.now();
            setTimeout(function(){
                done();
              }, (MAX_TIME_BEFORE_TRAFFIC_JAM+2)*1000);
        });

        it('Should detect traffic jam after 5 seconds waiting',function(done){
              infrared_function.lookfor_traffic_jam();
            assert.strictEqual(infrared_function.have_traffic_jam,true);
            done();
        });
        
          
    });

    describe('#loop() : Main device loop', function () {

        it('The time detecting the car should last 2 seconds after reading two following high value',function(done){
            for(var i =0; i<4;i++){infrared_function.loop()}
            assert.strictEqual(infrared_function.time_last,2);
            done();
        });
        it('The time detecting the car should last 6 seconds after reading six following high value',function(done){
            for(var i =0; i<4;i++){infrared_function.loop()}
            assert.strictEqual(infrared_function.time_last,6);
            done();
        });
        it('The time detecting the car should finish after reading low value',function(done){
            infrared_function.have_traffic_jam=false;
            infrared_function.loop();
            assert.strictEqual(infrared_function.time_last,0);
            done();
        });
        
          
    });
});
