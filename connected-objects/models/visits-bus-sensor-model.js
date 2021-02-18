function presence(port_server_shelter,port_server_this_sensor,id,values_presence_enter,values_presence_out,max_people,spead_read_value){
  const express=require("express");
  const app =express();
  app.use(
    express.urlencoded({
      extended: true
    })
  )
  
  app.use(express.json())
  const axios = require('axios');
  let index_state_in=0;
  let index_state_out=0;
  let nbPerson=0;
  let nbValuesIn=values_presence_enter.length;
  let nbValuesOut=values_presence_out.length;
  async function full() {
    await axios.post('http://localhost:'+port_server_shelter+'/busfull',  {
          id: id,
          nbPerson:nbPerson
        })
        .then(function (response) {
          console.log(response.data);
        })
        .catch(function (error) {
          console.log(error);
        });
  }

  let haveMadeRequest=false;
  function count(){
      nbPerson+=values_presence_enter[index_state_in%nbValuesIn];
      nbPerson-=values_presence_out[index_state_out%nbValuesOut];
      console.log(nbPerson+' in the bus.');
      if(
        (nbPerson>max_people)&&
        haveMadeRequest==false
        ){
          full();
          haveMadeRequest=true;
      }
      else{
        haveMadeRequest=false;
      }
      index_state_in++;
      index_state_out++;
  }
  app.get('/',(req,res)=>{
    let value={id:id,nbPerson:nbPerson}
    res.send(value);
  })
  app.listen(port_server_this_sensor,()=>{
    console.log("Visits bus sensor server up and running...")
    setInterval(count, spead_read_value);
  });
  
};
module.exports = presence