import 'Badge.dart';

///Model d'un utilisateur

class User {
  int uid;
  String username;
  String email;
  int score;
  int points;
  List<Badge> badges;
  

  User(Map<String, dynamic> map){
    uid = map["id"];
    username = map["username"];
    email = map["email"];
    score = map["score"];
    points = map["points"];
    badges = fromJsonToBadge(map["badges"]);
  }

  Map<String, dynamic> toMap(){
    return {
      "id" : uid,
      "username" : username,
      "email" : email,
    };
  }

  List<Badge> fromJsonToBadge(List<dynamic> map){
    if(map!=null) {
      List<Badge> badges = new List();
      map.forEach((element) {
        badges.add(Badge(element));
      });

      return badges;
    }
    else{
      return [];
    }
  }

}