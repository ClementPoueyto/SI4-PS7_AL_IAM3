function phones(port_server_shelter,values_connexions,speed_read_value){
    const axios = require('axios');
    async function connect() {
        await axios.post('http://localhost:'+port_server_shelter+'/connect')
          .then(function (response) {
            console.log(response.data);
          })
          .catch(function (error) {
            console.log(error);
          });
    }

    async function disconnect() {
        await axios.post('http://localhost:'+port_server_shelter+'/disconnect')
          .then(function (response) {
            console.log(response.data);
          })
          .catch(function (error) {
            console.log(error);
          });
    }
    let index_connexion_state=0;
    let nbConnexions=values_connexions.length;
    function loop(){
      if(values_connexions[index_connexion_state%nbConnexions]==1){
          connect();
      }
      if(values_connexions[index_connexion_state%nbConnexions]==2){
          disconnect();
      }

      index_connexion_state++;
    }
    setInterval(loop,speed_read_value)

}

module.exports=phones