class Badge {

  String image;
  String name;

  Badge(Map<String, dynamic> map) {
    this.image=map["image"];
    this.name = map["name"];
  }

}