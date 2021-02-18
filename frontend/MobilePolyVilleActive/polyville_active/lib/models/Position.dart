//model Position

class Pos{

  double latitude;
  double longitude;

  Pos(double lat, double lon){
    this.latitude=lat;
    this.longitude=lon;
  }

  Map<String, dynamic> toJson() =>
      {
        'latitude': this.latitude,
        'longitude': this.longitude,
      };


  @override
  String toString() {
    return 'Pos{latitude: $latitude, longitude: $longitude}';
  }
}