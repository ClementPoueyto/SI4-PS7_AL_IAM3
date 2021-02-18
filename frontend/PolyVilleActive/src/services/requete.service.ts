import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { serverUrl } from '../configs/server.config';
import { Programme } from '../models/programme.model';

/**
 * Service de liaison avec le back et de gestion du contenu
 */
@Injectable({
  providedIn: 'root'
})
export class RequeteService {
  private rep: Programme;
  reponse$: BehaviorSubject<Programme> ;
  private programUrl = serverUrl + '/program';

  constructor(private http: HttpClient) {
    this.rep={"program":""} as Programme;
    this.reponse$= new BehaviorSubject(this.rep);
  }


requestBack(program: string, verify: boolean){
  this.rep={"program":"chargement de la réponse...."} as Programme;
  this.reponse$.next(this.rep);
  this.http.post<any>(this.programUrl, JSON.stringify({ "program": program.split(/\u00A0/g).join(' '),"verifyOnly":verify })).subscribe((rep) => {
      this.rep={"program":JSON.parse(JSON.stringify(rep))} as Programme;
      this.reponse$.next(this.rep);
    }, (error) => { 
      this.rep={"program":"Un problème est survenu, veuillez essayer plus tard"} as Programme;
      this.reponse$.next(this.rep); 
    });
 }
}

