const transport_service = require('../models/transport-service-model.js')
const CONFIG=require('../models/config.js'); 
const BREAKDOWNS=[
    {breakdown:false },
    {breakdown:false },
    {breakdown:false },
    {breakdown:false },
    {breakdown:false },
    {breakdown:true ,line:"Aéroport CDG 2 / Mitry Claye - Robinson / St-Rémy lès Chevreuse",num:"2",delay_time:300000},
    {breakdown:false },
    {breakdown:false },
    {breakdown:false }
]
const request = require('supertest');
const { assert } = require('chai');

var transport_service_function= new transport_service(CONFIG.port_server_shelter_1,BREAKDOWNS)

describe("Test transport service server ", function() {

    describe('Getting status line', function () {
        it('Breakdown still not happened', function (done) {
            request(transport_service_function.app)
                .get('/')
                .set('Accept', 'application/json')
                .expect('Content-Type', /json/)
                .expect(function(res) {
                    assert.equal(res.body.breakdown,BREAKDOWNS[0].breakdown)
                  })
                  .expect(200,done);
                
        });
        transport_service_function.index_value+=4;
        it('Breakdown happenned', function (done) {
            request(transport_service_function.app)
                .get('/')
                .set('Accept', 'application/json')
                .expect('Content-Type', /json/)
                .expect(function(res) {
                    assert.equal(res.body.breakdown,BREAKDOWNS[5].breakdown)
                })
                .expect(200,done);
        });
    });
});