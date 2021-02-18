import 'package:polyvilleactive/models/ModeTransport.dart';

import 'Place.dart';
import 'Section.dart';

class Journey {

   Place arrival;
   Place departure;
   DateTime dateDeparture;
   DateTime dateArrival;
   int duration;
   List<Section> sections;
   int nbTransfers;
   double fare;
   double co2emission;
   Map<ModeTransport, int> transports;

   Journey(Map<String, dynamic> map) {
      arrival = Place(map["arrival"]);
      departure = Place(map["departure"]);
      dateDeparture = fromJsonToDateTime(map["dateDeparture"]);
      dateArrival = fromJsonToDateTime(map["dateArrival"]);
      duration = map["duration"];
      nbTransfers = map["nbTransfers"];
      fare = map["fare"];
      co2emission = map["co2emission"];
      transports = fromJsonToTransports(map["transports"]);
      sections = fromJsonToSections(map["sections"]);
   }

   List<Section> fromJsonToSections(List<dynamic> map){
      if(map!=null) {
         List<Section> sections = new List();
         map.forEach((element) {
            sections.add(Section(element));
         });

         return sections;
      }
      else{
         return [];
      }
   }

   Map<ModeTransport, int> fromJsonToTransports(Map<String, dynamic> map){
      Map<ModeTransport, int> res = new Map();
      map.forEach((key, value) {
         res.putIfAbsent(ModeTransportExtension.stringToTransport(key), () => value);
      });

      return res;
   }

   DateTime fromJsonToDateTime(Map<String, dynamic> map) {
      return new DateTime(
          map["year"], map["monthValue"], map["dayOfMonth"], map["hour"],
          map["minute"], map["second"]);
   }


}