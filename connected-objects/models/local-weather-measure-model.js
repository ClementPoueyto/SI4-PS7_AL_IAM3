function local_weather(port_server_this_sensor,type,district_name,values_general){
    var self =this;
    const express=require("express");
    self.app =express();
    self.app.use(
      express.urlencoded({
        extended: true
      })
    )
    
    self.app.use(express.json())
    
    self.index_value=0;
    self.nbValues=values_general.length;
    self.app.get('/',(req,res)=>{
        let value = {main:values_general[self.index_value%self.nbValues],district_name:district_name ,weather_type:type};
        res.send(value);
        self.index_value++;
    })
    self.start=function(){
      self.app.listen(port_server_this_sensor,()=>{
          console.log("Local weather sensors server up and running...");
      });
    }
    
}
module.exports =local_weather