import 'package:polyvilleactive/models/ModeTransport.dart';

import 'Place.dart';

class Section{
   Place from;
   Place to;

   int duration;

   DateTime arrivalDateTime;
   DateTime departureDateTime;
   String line;
   String numero;
   ModeTransport modeTransport;
   double co2emission;
   bool reached=false;

   Section(Map<String, dynamic> map){

      reached=false;
      if(map["from"]!=null) {
         from = Place(map["from"]);
      }
      if(map["to"]!=null) {
         to = Place(map["to"]);
      }
      duration = map["duration"];
      arrivalDateTime = fromJsonToDateTime(map["arrivalDateTime"]);
      departureDateTime = fromJsonToDateTime(map["departureDateTime"]);

      if(map["transport"]!=null) {
         modeTransport = ModeTransportExtension.stringToTransport(map["transport"]["modeTransport"]);
         if(map["transport"]["numero"]!=null){
            numero=map["transport"]["numero"];
         }
         if(map["transport"]["line"]!=null){
            line=map["transport"]["line"];
         }
      }
         co2emission = map["co2emission"];

   }

   DateTime fromJsonToDateTime(Map<String, dynamic> map) {
      return new DateTime(
          map["year"], map["monthValue"], map["dayOfMonth"], map["hour"],
          map["minute"], map["second"]);
   }


}