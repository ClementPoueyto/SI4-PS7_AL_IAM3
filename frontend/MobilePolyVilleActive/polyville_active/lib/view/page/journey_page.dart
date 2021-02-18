import 'package:flutter/material.dart';
import 'package:polyvilleactive/models/Demands.dart';
import 'package:polyvilleactive/models/Journey.dart';
import 'package:polyvilleactive/models/ModeTransport.dart';
import 'package:polyvilleactive/models/Position.dart';
import 'package:polyvilleactive/models/ResponseDemands.dart';
import 'package:polyvilleactive/models/Section.dart';
import 'package:polyvilleactive/models/UserForm.dart';
import 'package:polyvilleactive/models/user.dart';
import 'package:polyvilleactive/my_material.dart';
import 'package:polyvilleactive/util/alert_helper.dart';
import 'package:polyvilleactive/util/journey_helper.dart';
import 'package:polyvilleactive/view/tiles/SectionTile.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:trust_location/trust_location.dart';
import 'package:polyvilleactive/util/map_helper.dart';
import 'package:location_permissions/location_permissions.dart';

class JourneyPage extends StatefulWidget {
  final Journey journey;
  final List<ModeTransport> filters;
  final bool green;

  JourneyPage({this.journey, this.filters, this.green});
  _JourneyState createState() => _JourneyState();
}

class _JourneyState extends State<JourneyPage> implements Dialog_button_ask_handler {
  Journey journey;
  String _latitude;
  String _longitude;
  List<ModeTransport> filters;
  bool green;

  @override
  void initState() {
    super.initState();
    this.journey = widget.journey;
    this.filters = widget.filters;
    this.green = widget.green;

    requestLocationPermission();
    // input seconds into parameter for getting location with repeating by timer.
    // this example set to 5 seconds.
    TrustLocation.start(5);
    getLocation();

  }

  Future<void> getLocation() async {
    try {
      TrustLocation.onChange.listen((values) => setStateIfMounted(() {
        _latitude = values.latitude;
        _longitude = values.longitude;
        checkAround();
        checkIncidents();
        checkDemands();
      }));
    } on PlatformException catch (e) {
      print('PlatformException $e');
    }
  }
  void setStateIfMounted(f) {
    if (mounted) setState(f);
  }

  /// request location permission at runtime.
  void requestLocationPermission() async {
    PermissionStatus permission =
    await LocationPermissions().requestPermissions();
    print('permissions: $permission');
  }

  @override
  void dispose() {
    TrustLocation.stop();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: const Text('Itinéraire'),
        ),
        body: ListView.builder(
          itemCount: journey.sections.length + 1,
          itemBuilder: (BuildContext ctx, int index) {
            if (index == 0) {
              return Center(
                child  :Container(
                  height: 200,
                child :Card(
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(20.0),
                  ),
                  elevation: 10,
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: <Widget>[
                      PaddingWith(left:15, right: 15,widget: MyText("De : "+journey.departure.address)),
                      PaddingWith(left:15, right: 15,widget: Text("à "+journey.dateDeparture.hour.toString()+":"
                          +(journey.dateDeparture.minute<10?"0"+journey.dateDeparture.minute.toString():journey.dateDeparture.minute.toString()))),

                      const SizedBox(width: 8),

                      PaddingWith( left:15, right: 15,widget : MyText("Jusqu'à : "+journey.arrival.address)),
                      PaddingWith(left:15, right: 15,widget: Text("à "+journey.dateArrival.hour.toString()+":"
                          +(journey.dateArrival.minute<10?"0"+journey.dateArrival.minute.toString():journey.dateArrival.minute.toString()))),
                      const SizedBox(width: 8),
                    ],
                  ),
                ),
              ),);
            } else {
              Section section = journey.sections[index - 1];

              return PaddingWith(
                  top: 0, bottom: 10, widget: SectionTile(section: section));
            }
          },
        ));
  }

  void checkAround(){
    for(Section section in this.journey.sections){
      if(section.from!=null) {
        if (MapHelper().calculateDistance(section.to.pos.latitude.toDouble(),
            section.to.pos.longitude.toDouble(), double.parse(_latitude),
            double.parse(_longitude)) < 0.2) {
          print(MapHelper().calculateDistance(section.to.pos.latitude.toDouble(),
              section.to.pos.longitude.toDouble(), double.parse(_latitude),
              double.parse(_longitude)));
          callStep(section);
        }
      }
    }
  }

  void callStep(Section section) async {
    if(section.reached!=true){
      section.reached=true;

      SharedPreferences prefs = await SharedPreferences.getInstance();
      int id =prefs.getInt('id');
      JourneyHelper().validateJourneyStep(id, section.to.pos, UserForm(filters, green)).then((value) => {
        if(value=="OVER"){
          AlertHelper().error(context, "Super !", "Vous êtes arrivés à destination")
        }
      });
    }
  }

  Future<void> checkIncidents() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    int id =prefs.getInt('id');
    JourneyHelper journeyHelper = JourneyHelper();
    journeyHelper.getIncidents(id).then((value) => {
      if(value.length>0){
        journeyHelper.getJourneyInfo(Pos(double.parse(_latitude),double.parse(_longitude)), journey.arrival.pos, id, filters, green).then((newJourney) => {
          if(newJourney!=null){
            this.setStateIfMounted((){
              this.journey=newJourney;
                AlertHelper().error(context, value.length.toString()+" incidents sur votre trajet", value[0].line);
            })
          }
        })
      }
    });
  }

  Demands demands;
  Future<void> checkDemands() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    int id =prefs.getInt('id');
    JourneyHelper journeyHelper = JourneyHelper();
    journeyHelper.getDemands(id).then((value) => {
      if(value.length>0){
        demands=value[0],
        AlertHelper().ask(context,this,
            "Demande place", "Voulez-vous laissez votre place pour une autre personne?")
      }
    });
  }

  @override
  Future<String> accept() async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    int id =prefs.getInt('id');
    JourneyHelper journeyHelper = JourneyHelper();
    ResponseDemands responseDemands = new ResponseDemands(demands.id, true);
    journeyHelper.answerDemands(id,responseDemands).then((value) => {
      if(value==1){
        journeyHelper.getJourneyInfo(Pos(double.parse(_latitude),double.parse(_longitude)), journey.arrival.pos, id, filters, green).then((newJourney) => {
          if(newJourney!=null){
            this.setStateIfMounted((){
              this.journey=newJourney;
            })
          }
        }),
        AlertHelper().error(context,"Réponse serveur ", "Bien compris. Merci à vous!")

      },
      if(value==0){
        AlertHelper().error(context,"Réponse serveur ", "La demande a déjà été traité.")
      },
      if(value==3){
        AlertHelper().error(context,"Réponse serveur ", "La demande a expiré.")
      }
    });
    return "Accepting demands";
  }

  @override
  Future<String> refuse() async{
    SharedPreferences prefs = await SharedPreferences.getInstance();
    int id =prefs.getInt('id');
    JourneyHelper journeyHelper = JourneyHelper();
    ResponseDemands responseDemands = new ResponseDemands(demands.id, false);
    journeyHelper.answerDemands(id,responseDemands).then((value) => {
      AlertHelper().error(context,"Réponse serveur ", "Bien compris.")
    });
    return "Refusing demands.";
  }



}
