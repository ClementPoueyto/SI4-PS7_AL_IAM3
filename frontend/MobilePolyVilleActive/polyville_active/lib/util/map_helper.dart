import 'package:geocoder/geocoder.dart';
import 'package:geolocator/geolocator.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:geocoding/geocoding.dart';
import 'dart:math' show cos, sqrt, asin;

///Permet de manipuler les données d'une Carte
class MapHelper {
  Position position;

  ///Verifie si la permission du téléphone autorise la localisation
  Future<bool> isLocationEnable() async {
    var status = await Permission.location.request();
    if (status == PermissionStatus.granted) {
      return true;
    } else {
      return false;
    }
  }

  ///Renvoie la position de l'utilisateur
  Future<Position> getPosition() async {
    if (await isLocationEnable()) {
      Position position = await Geolocator()
          .getCurrentPosition(desiredAccuracy: LocationAccuracy.high);
      this.position = position;
      return position;
    }

    return null;
  }

  ///Prend en compte la nouvelle position selectionnée par l'utilisateur et modifie l'adresse
  Future<List<Location>> getCoordonateFromAdress(String adress) async {
    print(adress);
    List<Location> locationsStart;
    try {
       locationsStart = await locationFromAddress(
          adress, localeIdentifier: "en");
    }
    catch(error){
      print(error);
    }
    return locationsStart;
  }

  double calculateDistance(lat1, lon1, lat2, lon2){
    var p = 0.017453292519943295;
    var c = cos;
    var a = 0.5 - c((lat2 - lat1) * p)/2 +
        c(lat1 * p) * c(lat2 * p) *
            (1 - c((lon2 - lon1) * p))/2;
    return 12742 * asin(sqrt(a));
  }

}
