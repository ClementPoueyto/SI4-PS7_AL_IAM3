const transport_service = require('./models/transport-service-model.js')
const CONFIG=require('./models/config.js'); 
const BREAKDOWNS=[
    {breakdown:false },
    {breakdown:false },
    {breakdown:false },
    {breakdown:false },
    {breakdown:false },
    {breakdown:true ,line:"Balard - Créteil-Pointe du Lac",num:"2",delay_time:300000},
    {breakdown:false },
    {breakdown:false },
    {breakdown:false }
]

var transport_service_function =new transport_service(CONFIG.port_server_transport_service,BREAKDOWNS);
transport_service_function.start();