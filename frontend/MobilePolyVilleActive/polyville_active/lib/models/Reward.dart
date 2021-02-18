class Reward {

  String title;
  int points;
  int id;
  Reward(Map<String, dynamic> map) {
    this.id = map["id"];
    this.title = map["title"];
    this.points = map["points"];
  }

}