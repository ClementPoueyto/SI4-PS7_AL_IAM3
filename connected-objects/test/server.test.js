const server = require('../models/server-model.js')
const MODE_TRANSPORT='BUS';
const CONFIG=require('../models/config.js'); 
const LOG_ERROR=false;
const request = require('supertest');
const { assert } = require('chai');
var serv= new server(MODE_TRANSPORT,CONFIG.port_server_shelter_1,CONFIG,LOG_ERROR);

describe("Test bus shelter server", function() {

    describe('Bluetooth devices connection', function () {
        it('Should increase the number of devices when bluetooth connect', function (done) {
            request(serv.app)
                .post('/connect')
                .set('Accept', 'application/json')
                .expect(function(res) {
                    assert.equal(serv.nBDevices,1)
                  })
                  .expect(200,done);
                  
        });
        it('Should decrease the number of connecion when bluetooth disconnect', function (done) {
            request(serv.app)
                .post('/disconnect')
                .set('Accept', 'application/json')
                .expect(function(res) {
                    assert.equal(serv.nBDevices,0)
                  })
                  .expect(200,done);
                  
        });
        
    });
});