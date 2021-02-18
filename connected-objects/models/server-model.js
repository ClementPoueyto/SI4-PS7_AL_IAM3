function server(mode_transport,this_port_server,config,log_error){
  var self=this;
  const express=require("express");
  self.app =express();
  const axios = require('axios');
 
  //Information connexion service weather
  //Route by city name : api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key} 
  const CITY_BUS_SHELTER='Paris,fr';
  const KEY_WEATHER_SERVICE='8a2867f1bae4f24bac095f064e0a79e7';
  const ROUTE_WEATHER_SERVICE='http://api.openweathermap.org/data/2.5/weather?q='+CITY_BUS_SHELTER+'&appid='+KEY_WEATHER_SERVICE;
  const TIME_SPEED_GET_WEATHER=1000; //Milliseconds

  //Informations connexion backend application
  const BASE_ROUTE_APP='http://localhost:'+config.port_server_app+'/busshselter/'
  const ROUTE_DELAY='delay' //Retard traffic
  const ROUTE_FAST='fast' //Le bus est passé en avance
  const ROUTE_FULL='full' //Trop de personnes sur le traffic
  const ROUTE_WEATHER='weather' //Changement de temps météo sur le chemin
  const ROUTE_BREAKDOWN='breakdown' // Panne

  //Mode de transport derservi près de la borne
  self.mode_transport=mode_transport;

  //Liste des lignes
  const TABLE_LINES=[
    {id:123,name:"Mairie du XVIIIè Julles Joffrin - Porte de Versailles",num:"2", districts:['District']},
    //{id:1234,name:"Mairie d'Issy - Front populaire",num:"2", districts:['District']}

  ]
  //Liste des capteurs que la borne écoute
  const TABLE_SENSORS=[
    {id_sensor:"T64YJ",id_line:123}
  ]

  //Récupération de la date du jour
  let TODAY= new Date();
  const TODAY_YEAR=TODAY.getFullYear();
  const TODAY_MONTH=TODAY.getMonth();
  const TODAY_DAY=TODAY.getDate();
  const BUS_PASSED_HOUR ={ hours:11, minutes:24, seconds:0};// Heure à laquelle le bus passera près de la borne. Sachant
  //qu'on ne contrôle pas l'heure du sytème on décide de l'heure de passage pour provoquer l'incident.

  //Horaire de la ligne de transport
  let hours_lines =[
    {
      id_sensor:"T64YJ",
      hours:[
        {time: new Date(TODAY_YEAR, TODAY_MONTH, TODAY_DAY, 08, 0, 0) ,passed:true },
        {time: new Date(TODAY_YEAR, TODAY_MONTH, TODAY_DAY, BUS_PASSED_HOUR.hours+5, BUS_PASSED_HOUR.minutes+5, BUS_PASSED_HOUR.seconds+5)  ,passed:false },
        {time: new Date(TODAY_YEAR, TODAY_MONTH, TODAY_DAY, 12, 0, 0) ,passed:false },
        {time: new Date(TODAY_YEAR, TODAY_MONTH, TODAY_DAY, 15, 0, 0) ,passed:false },
        {time: new Date(TODAY_YEAR, TODAY_MONTH, TODAY_DAY, 19, 0, 0) ,passed:false }
      ]
    }
  ]

  function local_date(){
    let date=new Date();
    var timeOffsetInMS = date.getTimezoneOffset() * 60000;
    date.setTime(date.getTime() - timeOffsetInMS);
    return date
  }

  function local_date_object(){
    return object_from_local_date(local_date());
  }
  function object_from_local_date(date){
    return {
          year:date.getFullYear(),
          month: date.getMonth()+1,
          date:date.getDate(),
          hours:date.getHours(),
          minutes:date.getMinutes(),
          seconds:date.getSeconds(),
          milliseconds:date.getMilliseconds()
    }
  }

  self.nBDevices=0; // Nombre d'appareil connecté à la borne

  self.app.use(
      express.urlencoded({
        extended: true
      })
    )
    
  self.app.use(express.json())

  self.get_line_from_sensor=function(id_sensor){
    let sensor =TABLE_SENSORS.filter( obj => obj.id_sensor == id_sensor);
    let line=TABLE_LINES.filter(obj => obj.id==sensor[0].id_line);
    return line[0];
  }

  self.get_line_from_district_name=function(district_name){
    let line_selected=undefined;
    TABLE_LINES.forEach((line)=>{
      let linefiltered=line.districts.filter(district=>district==district_name);
      if(linefiltered.length>0){
        return line_selected= line;}
    })
    return line_selected;
  }

  self.notify_delay=async function (id,delay_time) {
      let line =self.get_line_from_sensor(id);
      //console.log(delay_time/(1000*60))
      let incident ={time:delay_time,
        line:line.name,
        num:line.num,
        mode_transport:self.mode_transport,
        timestamp: local_date_object() }
      await axios.post(BASE_ROUTE_APP+ROUTE_DELAY, incident)
        .then(function (response) {
          console.log(response.data);
        })
        .catch(function (error) {
          if(log_error==true){
            console.log(error);
          }
        });
  }

  self.notify_breakdown=async function (breakdown) {
    let incident ={
      time:breakdown.delay_time,
      line:breakdown.line,
      num:breakdown.num,
      mode_transport:self.mode_transport }
    await axios.post(BASE_ROUTE_APP+ROUTE_BREAKDOWN, incident)
      .then(function (response) {
        console.log(response.data);
      })
      .catch(function (error) {
        if(log_error==true){
          console.log(error);
        }
      });
  }

  self.notify_changement_weather=async function (weather_type,district_name) {
    let line =self.get_line_from_district_name(district_name);
    if(line==undefined){
      return;}
    let incident ={
      time:60000,
      line:line.name,
      num:line.num,
      type :weather_type,
      mode_transport:self.mode_transport ,
    timestamp: local_date_object()}
    await axios.post(BASE_ROUTE_APP+ROUTE_WEATHER, incident)
      .then(function (response) {
        console.log(response.data);
      })
      .catch(function (error) {
        if(log_error==true){
          console.log(error);
        }
      });
  }

  self.notify_incident_transport=async function (id,route,delay_time) {
    let line =self.get_line_from_sensor(id);
    let incident ={
      time:delay_time,
      line:line.name,
      num:line.num,
      mode_transport:self.mode_transport ,
      timestamp: local_date_object()
    }
    await axios.post(BASE_ROUTE_APP+route, incident)
      .then(function (response) {
        console.log(response.data);
      })
      .catch(function (error) {
        if(log_error==true){
          console.log(error);
        }
      });
  }

  self.isAhead=function(id){
      let hours_line =hours_lines.filter(obj=>obj.id_sensor==id);
      let bus_not_passed=hours_line[0].hours.filter(obj=>obj.passed==false);
      let margin_time = new Date(TODAY_YEAR, TODAY_MONTH, TODAY_DAY, BUS_PASSED_HOUR.hours, BUS_PASSED_HOUR.minutes, BUS_PASSED_HOUR.seconds)-bus_not_passed[0].time;
      return margin_time;
  }

  self.app.get('/',(req,res)=>{
      res.send('Hello World!')
  })

  self.app.post('/trafficjam',(req,res)=>{
      res.send('The busshelter got the notifaction of the traffic jam.');
      self.notify_delay(req.body.id,req.body.time);
  })

  self.app.post('/buspassed',(req,res)=>{
    res.send('The busshelter got the notifaction that the bus passed ahead.');
    let delay=self.isAhead(req.body.id);
    if(delay<0){
      delay=delay*(-1)*60000;
      self.notify_incident_transport(req.body.id,ROUTE_FAST,delay);
    } 
  })

  self.app.post('/busfull',(req,res)=>{
    res.send('The busshelter got the notifaction that the bus is full.');
    let delay =(self.nBDevices+req.data.nbPerson)*60000;
    self.notify_incident_transport(req.body.id,ROUTE_FULL,delay);
    
  })

  self.app.get('/',(req,res)=>{
    res.send('The busshelter got the notifaction of the breakdown from transport service.');
    self.notify_breakdown(req.body.id,ROUTE_BREAKDOWN);
  })

  self.app.post('/connect',(req,res)=>{
    self.nBDevices++;
    res.send('Got bluetooth signal. Number of devices connected : ' + self.nBDevices);
    axios.get('http://localhost:'+config.port_server_visits_bus_sensor+'/')
      .then(function (response) {
        if(response.data.nbPerson+self.nBDevices>config.max_people){
          let delay =(self.nBDevices+response.data.nbPerson)*60000;
          self.notify_incident_transport(response.data.id,ROUTE_FULL,delay);
        }
      })
      .catch(function (error) {
        if(log_error==true){
        console.log(error);}
      });
  })

  self.app.post('/disconnect',(req,res)=>{
    self.nBDevices--;
      res.send('Lost bluetooth signal. Number of devices connected : ' + self.nBDevices);
  })


  self.requestStateLine=function(){
    axios.get('http://localhost:'+config.port_server_transport_service+'/')
    .then(function (response) {
      console.log(response.data)
      if(response.data.breakdown==true){
        self.notify_breakdown(response.data);
      }
    })
    .catch(function (error) {
      if(log_error==true){
        console.log(error);}
    });
  }

  self.approximate=function(excepted,value,delta){
    if(value<excepted+delta && value>excepted-delta){
      return true;
    }
    return false;
  }

  let mockWeatherType='Rain';
  self.isThereWeatherChanges=function(api_weather_data,local_weather_data){
      api_weather_data.main.humidity=100.0;
      api_weather_data.main.pressure=901.0;
      api_weather_data.main.temp=8.1;
      if(self.approximate(api_weather_data.main.humidity,local_weather_data.main.humidity,5)&&
      self.approximate(api_weather_data.main.pressure,local_weather_data.main.pressure,5) &&
      self.approximate(api_weather_data.main.temp,local_weather_data.main.temp,2) 
        ){
            return {state:true};
        }

        return {state:false};
  }

  self.requestWeather= function (data_api){
    axios.get('http://localhost:'+config.port_server_local_weather+'/')
    .then(function (response) {
      let weather_change=self.isThereWeatherChanges(data_api,response.data);
      console.log(weather_change);
      if(weather_change.state==true){
        self.notify_changement_weather(response.data.weather_type,response.data.district_name);
      }
    })
    .catch(function (error) {
      if(log_error==true){
        console.log(error);}
    });
  }

  self.weather= function (){
    axios.get(ROUTE_WEATHER_SERVICE)
    .then(function (response) {
      self.requestWeather(response.data);
    })
    .catch(function (error) {
      if(log_error==true){
      console.log(error);}
    });
  }

  self.start= function(){
    self.app.listen(this_port_server,()=>{
      console.log("Server up and running...");
      if(process.argv.length>2){
        var myArgs = process.argv.slice(2);

        for(var i =0;i<myArgs.length;i++){
          switch (myArgs[i]) {
            case '-weather':
              setInterval(self.weather,TIME_SPEED_GET_WEATHER);
              break;
            case '-breakdown':
              setInterval(self.requestStateLine,TIME_SPEED_GET_WEATHER);
              break;
            case '-transport':
              self.mode_transport=myArgs[i+1];
              break;
          }
        }
       
      }      
    });
  }
}
module.exports=server