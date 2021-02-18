function presence(port_server_shelter,id,values_presence,spead_read_value){
  var self=this;
  const axios = require('axios');
  let index_state=0;
  let nbValues=values_presence.length;
  async function makeRequest() {
    await axios.post('http://localhost:'+port_server_shelter+'/buspassed',  {
          id: id
        })
        .then(function (response) {
          console.log(response.data);
        })
        .catch(function (error) {
          console.log(error);
        });
  }

  function count(){
      if(values_presence[index_state%nbValues]==1)
      {
       makeRequest();
      }
      index_state++;
  }
  setInterval(count, spead_read_value);

};
module.exports = presence