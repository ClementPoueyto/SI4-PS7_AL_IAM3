import 'package:polyvilleactive/models/ModeTransport.dart';
class Incident {

  ModeTransport transport;
  String line;
  String numero;

  Incident(Map<String, dynamic> map) {
    this.line=map["line"];
    this.transport = ModeTransportExtension.stringToTransport(map["mode_transport"]);
  }

}