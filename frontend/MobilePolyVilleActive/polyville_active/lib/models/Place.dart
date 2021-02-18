import 'Position.dart';

class Place{
  String address;
  Pos pos;

  Place(Map<String, dynamic> map){
    this.pos=Pos(map["pos"]["latitude"],map["pos"]["longitude"]);
    this.address=map["adress"];
  }


}