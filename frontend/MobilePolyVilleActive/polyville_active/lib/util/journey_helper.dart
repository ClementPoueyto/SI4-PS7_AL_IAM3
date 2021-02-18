import 'dart:convert';
import 'package:flutter/cupertino.dart';
import 'package:http/http.dart' as http;
import 'package:polyvilleactive/models/Demands.dart';
import 'package:polyvilleactive/models/Journey.dart';
import 'package:polyvilleactive/models/ModeTransport.dart';
import 'package:polyvilleactive/models/Position.dart';
import 'package:polyvilleactive/models/ResponseDemands.dart';
import 'package:polyvilleactive/models/UserForm.dart';
import 'package:polyvilleactive/my_material.dart';
import 'package:polyvilleactive/models/Incident.dart';
import 'package:polyvilleactive/util/alert_helper.dart';


///Permet de manipuler les données de Navitia
class JourneyHelper {

  Future<Journey> getJourneyInfo(Pos start, Pos end, int id, List<ModeTransport> filters, isGreen) async{

    UserForm form = UserForm(filters, isGreen);
    print(form.objectToJson());
    bool mock = false;
    if(start==null||end==null){
      mock = true;
    }
  dynamic res;
    try {
      final resultat = await http.post(urlApi + "journey",
          headers: {
            "mock": mock.toString(),
            "id": id.toString(),
            "Accept": "application/json",
            'Content-Type': 'application/json; charset=UTF-8'
          },
          body: jsonEncode(<String, dynamic>{
            'from': start!=null?start.toJson():Pos(0,0).toJson(),
            'to': end!=null?end.toJson():Pos(0,0).toJson(),
            'form': form.objectToJson()

          }));
    if(resultat.statusCode==200) {
      res = json.decode(utf8.decode(resultat.bodyBytes));
    }
    else{
      return null;
    }
    } catch (error) {
      print(error);
    }
  print(res);
    return Journey(new Map<String, dynamic>.from(res));
  }


  Future<List<Incident>> getIncidents(int id) async{
    List<dynamic> res;
    List<Incident> incidents=new List();
    try {
      final resultat = await http.get(urlApi + "incidents?id="+id.toString(),
          headers: {
            "Accept": "application/json",
            'Content-Type': 'application/json; charset=UTF-8'
          });

      if(resultat.statusCode==200) {
        print(resultat.body.toString());

        res = json.decode(utf8.decode(resultat.bodyBytes));
        print(res);
        if(res.length>0){
          for(Map<String,dynamic> map in res){
            incidents.add(new Incident(map));
          }
        }
      }
      else{
        print(resultat.body.toString());

        return [];
      }
    } catch (error) {
      print(error);
      print(error);
    }
    return incidents;
  }

  Future<List<Demands>> getDemands(int id) async{
    Map<String,dynamic> res;
    List<Demands> demandsList=new List();
    print(urlApi + "demands?id="+id.toString());
    try {
      final resultat = await http.get(urlApi + "demands?id="+id.toString(),
          headers: {
            "Accept": "application/json",
            'Content-Type': 'application/json; charset=UTF-8'
          });
      if(resultat.statusCode==200) {
        print(resultat.body.toString());

        res = json.decode(utf8.decode(resultat.bodyBytes));
        if(res.length>0){
          demandsList.add(new Demands(res));
        }
      }
      else{
        print(resultat.body.toString());
        return [];
      }
    } catch (error) {
      print("Error : "+error.toString());
    }
    return demandsList;
  }

  Future<dynamic>answerDemands(int id,ResponseDemands responseDemands) async{

    dynamic res;
    try {
      final resultat = await http.post(urlApi + "demands/response",
          headers: {
            "id": id.toString(),
            "Accept": "application/json",
            'Content-Type': 'application/json; charset=UTF-8'
          },
          body: jsonEncode(responseDemands.toJson()));
      if(resultat.statusCode==200) {
        res = json.decode(utf8.decode(resultat.bodyBytes));
      }
      else{
        return null;
      }
    } catch (error) {
      print(error);
    }
    print(res);
    return res;
  }

  Future<String> validateJourneyStep(int userId,Pos endPosition, UserForm userForm) async{
    try {
      String route = urlApi+"validatedJourneyStep";
      final resultat = await http.post(route+"?id="+userId.toString(),
          headers: {
            "Accept": "application/json",
            'Content-Type': 'application/json; charset=UTF-8'
          },
          body: jsonEncode(<String, dynamic>{
            'endPosition': endPosition.toJson(),
            'form' : userForm.objectToJson()
          })
      );

      if(resultat.statusCode==200) {
        print(resultat.body.toString());
           return resultat.body.toString();
        }


    } catch (error) {
      print(error);
    }
  }


}