

function infrared(port_server_shelter,id,values_infra_red_light,max_time_traffic,spead_read_value,log_error){
  var self = this;
  const axios = require('axios');
  self.lateness=300000; //5 Minutes
  self.makeRequest = async function(time_laps){
    await axios.post('http://localhost:'+port_server_shelter+'/trafficjam',  {
        id: id,
        time:time_laps*self.lateness,
      })
      .then(function (response) {
        console.log(response.data);
      })
      .catch(function (error) {
        if(log_error==true){
        console.log(error);}
      });
  }


  const  HIGH=1
  const  LOW=0;
  self.infra_red_light_state = LOW;      // We start, assuming no motion detected
  self.size =values_infra_red_light.length;
  self.time_last=0; 
  self.last_time_car_detected=Date.now();
  self.max_time=max_time_traffic; // Depassing this time (in seconds) , there s a traffic jam

  self.index_value=0;
  self.digitalRead=function(){
    let val =values_infra_red_light[self.index_value%self.size]
    self.index_value++;
      return val;
  }

  self.have_traffic_jam=false;
  self.lookfor_traffic_jam=function(){
    if(
      (Date.now()-self.last_time_car_detected>(self.max_time*1000) ) &&
      self.have_traffic_jam==false
    ){
      self.have_traffic_jam=true;
  
    } 
  }

  self.loop=function(){
      var value = self.digitalRead();  // read input value
      if (value == HIGH) {            // check if the input is HIGH
        if (self.infra_red_light_state == LOW) {
            self.last_time_car_detected=Date.now();
            // we have just turned on
            //console.log("Motion started!")
            // We only want to print on the output change, not state
            self.infra_red_light_state = HIGH;
          }
          self.time_last++;
          self.lookfor_traffic_jam();
        } else {  
            
            if (self.infra_red_light_state == HIGH){
              if(self.have_traffic_jam){
                self.makeRequest(self.time_last);
                self.have_traffic_jam=false;
              }
              // we have just turned of
              //console.log("Motion ended!")
              // We only want to print on the output change, not state
              self.infra_red_light_state = LOW;
            }
            self.time_last=0;
    }
  }


  self.start = function(){
    setInterval(self.loop, spead_read_value);
  }
 
}

module.exports = infrared
