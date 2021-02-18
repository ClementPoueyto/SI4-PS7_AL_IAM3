const { exec } = require("child_process");
const folder = './infra-red-lights/';
const fs = require('fs');

function execute(command){
  exec(command, (error, stdout, stderr) => {
    if (error) {
        console.log(`error: ${error.message}`);
        return;
    }
    if (stderr) {
        console.log(`stderr: ${stderr}`);
        return;
    }
    console.log(`stdout: ${stdout}`);
  });
}

fs.readdir(folder, (err, files) => {
  let command="concurrently \"node server.js\" ";
  files.forEach(file => {
    command+="\"node "+folder+file+"\" ";
  });
  console.log(command)
  execute(command);
});



