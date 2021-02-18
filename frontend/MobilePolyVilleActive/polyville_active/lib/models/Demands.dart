import 'package:polyvilleactive/models/ModeTransport.dart';

import 'Journey.dart';
class Demands {

  Journey journeyAffected;
  int id;
  int idUserAsking;
  bool accepted;

  Demands(Map<String, dynamic> map) {
    this.id=map["id"];
    this.idUserAsking=map["idUserAsking"];
    this.accepted=map["accepted"];

  }

}