const local_weather = require('../models/local-weather-measure-model.js')
const request = require('supertest');
const CONFIG=require('../models/config.js');
const DISTRICT_NAME='District'
var type='Snow'
//Valeur de l'humidité dans l'air, pression et température à t instant
const VALUES_GENERAL=[
    {humidity:44.5,pressure:1024.0,temp:12.0},
    {humidity:67.2,pressure:1015.0,temp:11.0},
    {humidity:75.7,pressure:1011.1,temp:10.6},
    {humidity:66.3,pressure:1013.0,temp:13.1},
    {humidity:100.0,pressure:900.0,temp:8.4},
    {humidity:95.9,pressure:995.3,temp:10.8},
    {humidity:70.0,pressure:1011.7,temp:11.0},
];
var local_weather_function = new local_weather(
    CONFIG.port_server_local_weather,
    type,
    DISTRICT_NAME,
    VALUES_GENERAL
    );

describe("Test weather server", function() {

    describe('Getting local weather value', function () {
        it('Should get the first value of the tab with district name', function (done) {
            request(local_weather_function.app)
                .get('/')
                .set('Accept', 'application/json')
                .expect('Content-Type', /json/)
                .expect({main:VALUES_GENERAL[0], district_name:DISTRICT_NAME, weather_type:type}, done);
        });
    });
});