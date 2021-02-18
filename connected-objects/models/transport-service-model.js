function transport_service(port_server_this_service,breakdowns){
  var self=this;
    const express=require("express");
    self.app =express();
    self.app.use(
      express.urlencoded({
        extended: true
      })
    )
    
    self.app.use(express.json())
    
    self.index_value=0;
    let nbValues=breakdowns.length;
    self.app.get('/',(req,res)=>{
        let value = breakdowns[self.index_value%nbValues];
        res.send(value);
        self.index_value++;
    })
    self.start=function(){
      self.app.listen(port_server_this_service,()=>{
          console.log("Transport service server up and running...");
      });
    }



}

module.exports=transport_service