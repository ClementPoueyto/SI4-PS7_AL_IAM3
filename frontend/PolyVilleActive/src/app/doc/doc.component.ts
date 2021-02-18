import { Component, OnInit } from '@angular/core';

/**
 * Compoonent de la page Configuration
 */
@Component({
  selector: 'doc-configuration',
  templateUrl: './doc.component.html',
  styleUrls: ['./doc.component.scss']
})
export class DocComponent implements OnInit {

  titres: string[];
  motcles: string[];
  transports : string[];
  aleas : string[];
  constructor() {
    this.titres= ["Mots clés","Mode de transport", "Aléa"];
    this.motcles= ["global","local","quand","alors","fin","interdire", "prioriser","eviter"];
    this.transports= ["bus","metro","train","tramway"];
    this.aleas= ["retard","rempli","panne","avance","neige","soleil","pluie"];
  }

  ngOnInit() {

  }



}