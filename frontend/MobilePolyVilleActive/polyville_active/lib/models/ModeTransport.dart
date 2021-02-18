import 'package:flutter/material.dart';

enum ModeTransport {
  WALKING,
  BIKE,
  METRO,
  BUS,
  TRAMWAY,
  WAITING,
  RER,
  TRAIN,
  PUBLIC_TRANSPORT}

extension ModeTransportExtension on ModeTransport {
  String get value {
    switch (this) {
      case ModeTransport.WALKING:
        return "WALKING";

      case ModeTransport.BIKE:
        return "BIKE";
      case ModeTransport.METRO:
        return "METRO";
      case ModeTransport.TRAIN:
        return "TRAIN";
      case ModeTransport.BUS:
        return "BUS";
      case ModeTransport.TRAMWAY:
        return "TRAMWAY";
      case ModeTransport.RER:
        return "RER";
      case ModeTransport.WAITING:
        return "WAITING";
      case ModeTransport.PUBLIC_TRANSPORT:
        return "PUBLIC_TRANSPORT";
      default:
        return "";
    }
  }

  String get translate {
    switch (this) {
      case ModeTransport.WALKING:
        return "MARCHE";

      case ModeTransport.BIKE:
        return "VELO";
      case ModeTransport.METRO:
        return "METRO";
        case ModeTransport.TRAIN:
        return "TRAIN";
      case ModeTransport.BUS:
        return "BUS";
      case ModeTransport.TRAMWAY:
        return "TRAMWAY";
      case ModeTransport.RER:
        return "RER";
      case ModeTransport.WAITING:
        return "ATTENTE";
      case ModeTransport.PUBLIC_TRANSPORT:
        return "TRANSPORT PUBLIC";
      default:
        return "autre";
    }
  }

  Icon get icon {
    switch (this) {
      case ModeTransport.WALKING:
        return Icon(Icons.directions_walk);
      case ModeTransport.BIKE:
        return Icon(Icons.directions_bike);
      case ModeTransport.METRO:
        return Icon(Icons.directions_transit);
      case ModeTransport.BUS:
        return Icon(Icons.directions_bus);
      case ModeTransport.TRAMWAY:
        return Icon(Icons.tram);
      case ModeTransport.TRAIN:
        return Icon(Icons.train);
      case ModeTransport.WAITING:
        return Icon(Icons.watch_later);
      case ModeTransport.PUBLIC_TRANSPORT:
        return Icon(Icons.public);
      case ModeTransport.RER:
        return Icon(Icons.train);
      default:
        return null;
    }
  }

  static stringToTransport(String transport) {
    switch (transport) {
      case "WALKING":
        return ModeTransport.WALKING;
      case "BIKE":
        return ModeTransport.BIKE;
      case "TRAIN":
        return ModeTransport.TRAIN;
      case "METRO":
        return ModeTransport.METRO;
      case "BUS":
        return ModeTransport.BUS;
      case "TRAMWAY":
        return ModeTransport.TRAMWAY;
      case "RER":
        return ModeTransport.RER;
      case "WAITING":
        return ModeTransport.WAITING;
      case "PUBLIC_TRANSPORT":
        return ModeTransport.PUBLIC_TRANSPORT;
      default:
        return null;
    }
  }


}
