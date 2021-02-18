import { Component, OnInit } from '@angular/core';
import { RequeteService } from 'src/services/requete.service';
import { Programme } from '../../models/programme.model';

/**
 * Compoonent de la page Configuration
 */
@Component({
  selector: 'input-configuration',
  templateUrl: './input.component.html',
  styleUrls: ['./input.component.scss']
})
export class InputComponent implements OnInit {
	program: string;
  erreur: Programme;
  couleur: string;
  text: string;
  background: string;
  backgroundcolor: string;

  constructor(public requeteService: RequeteService) {
  this.requeteService.reponse$.subscribe((rep) => {
            this.erreur=rep;
        });


  }

  ngOnInit() {
  	this.program="";
    this.couleur ="black";
    this.text ="white";
    this.background="252525";
    this.backgroundcolor ="444444";

  }

changeColor(){
  if(this.couleur=="black"){
    this.couleur="white";
    this.text="black";
    this.background="CDCDCD";
    this.backgroundcolor ="EEEEEE";
  }else{
    this.couleur="black";
    this.text="white";
    this.background="252525";
    this.backgroundcolor ="444444";
  }

  document.body.style.background= "linear-gradient(135deg, #"+this.background+" 25%, transparent 25%) -50px 0,linear-gradient(225deg, #"+this.background+" 25%, transparent 25%) -49px 0,linear-gradient(315deg, #"+this.background+" 25%, transparent 100%),linear-gradient(45deg, #"+this.background+" 25%, transparent 25%)";
  document.body.style.backgroundColor= "#"+this.backgroundcolor;
  document.body.style.backgroundSize= "5em 5em";
}
  colorText(){
    //mots a highliter
    let keywords =["GLOBAL","LOCAL","QUAND","ALORS","FIN","INTERDIRE"];
    let alea =["RETARD","REMPLI","PANNE", "AVANCE","SOLEIL","PLUIE","NEIGE"];
    let transport =["BUS","METRO","TRAIN","TRAMWAY","TAXI"];

    let ligne=0,position=1;
     if ((<KeyboardEvent>event).keyCode == 32) {
        let str = (<HTMLInputElement>event.target).innerText;
        let nouveau = str.split('');
        let ancien=this.program.split('');
        let markups= "";
        str.split('\n').forEach(ligne => { // pour garder les sauts de ligne
          markups+='<div>';
          let motcle=keywords.map(w => `(${w})`).concat(alea.map(w => `(${w})`)).concat(transport.map(w => `(${w})`));
          let chunks = ligne.split(new RegExp(motcle.join('|'), 'i')).filter(Boolean),
            markup = chunks.reduce((acc, chunk) => {// pour coloriser les mots
              if(keywords.includes(chunk.toUpperCase())){
                chunk.split('').forEach(lettre =>{acc +=`<span class="statement">${lettre}</span>`;});
              }else if(alea.includes(chunk.toUpperCase())){
                chunk.split('').forEach(lettre =>{acc +=`<span class="alea">${lettre}</span>`;});
              }else if(transport.includes(chunk.toUpperCase())){
                chunk.split('').forEach(lettre =>{acc +=`<span class="transport">${lettre}</span>`;});
              }else{
                chunk.split('').forEach(lettre =>{acc +=`<span >${lettre}</span>`;});
              }
            return acc;
          }, '');
            markups+=markup;
          markups+='</div>';

        });
        (<HTMLInputElement>event.target).innerHTML = markups;

         //pour remettre le curseur, peut buguer si va trop vite
        for (var i = 0; i < nouveau.length - 1; i++) {
          if(nouveau[i]=='\n'){
            position=1;
            ligne++;
          }
          else if(nouveau[i]==ancien[i])
            position++;
          else
            break;
        }
        var child = (<HTMLInputElement>event.target).children;
        var range = document.createRange();
        var sel = window.getSelection();
        range.setStart(child[ligne], position );
        range.collapse(true);
        sel.removeAllRanges();
        sel.addRange(range);

        
        }
        this.program=(<HTMLInputElement>event.target).innerText;

    
  }
  sendProgram(verify : boolean){
    if (String(this.program)=="") {
      alert("No code written yet");
      return;
    }

    this.requeteService.requestBack(this.program,verify);
    this.requeteService.reponse$.subscribe((rep) => {
            this.erreur=rep;
  ;        });
  }

}