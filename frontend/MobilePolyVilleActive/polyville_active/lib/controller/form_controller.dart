import 'package:clip_shadow/clip_shadow.dart';
import 'package:flutter/material.dart';
import 'package:geocoder/geocoder.dart';
import 'package:geocoding/geocoding.dart';
import 'package:geolocator/geolocator.dart';
import 'package:polyvilleactive/models/ModeTransport.dart';
import 'package:polyvilleactive/my_material.dart';
import 'package:polyvilleactive/util/alert_helper.dart';
import 'package:polyvilleactive/util/journey_helper.dart';
import 'package:polyvilleactive/util/map_helper.dart';
import 'package:polyvilleactive/models/Position.dart';
import 'package:polyvilleactive/view/my_widgets/my_icon_button.dart';
import 'package:polyvilleactive/view/page/journey_page.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:trust_location/trust_location.dart';

import 'package:location_permissions/location_permissions.dart';

class FormController extends StatefulWidget {
  _FormState createState() => _FormState();
}

class _FormState extends State<FormController> {
  TextEditingController start;
  TextEditingController finish;
  final _formKey = GlobalKey<FormState>();
  Position position;
  List<Widget> icons;
  List<bool> selections;
  List<ModeTransport> filters;
  List<ModeTransport> transports;
  bool initiate = false;
  bool vert = false;

  @override
  void initState() {
    super.initState();
    icons = new List();
    transports = new List();
    filters = new List();
    start = TextEditingController();
    finish = TextEditingController();
  }


  /// request location permission at runtime.
  void requestLocationPermission() async {
    PermissionStatus permission =
        await LocationPermissions().requestPermissions();
    print('permissions: $permission');
  }

  @override
  void dispose() {
    start.dispose();
    finish.dispose();
    super.dispose();
  }

  Future<void> init() async {
      icons = new List();
      transports = new List();
      filters = new List();

    for(ModeTransport element in ModeTransport.values) {
      if (element != ModeTransport.PUBLIC_TRANSPORT &&
          element != ModeTransport.RER && element != ModeTransport.WALKING && element != ModeTransport.BIKE &&
          element != ModeTransport.WAITING) {
        icons.add(element.icon);
        transports.add(element);
      }
    }
      selections = List.generate(icons.length, (index) => true);
  }

  @override
  Widget build(BuildContext context) {
    int counter = 0;
    if(!initiate) {
      init();
      initiate=true;
    }
    return Scaffold(
      body: GestureDetector(
        onTap: () {
          hideKeyBoard();
        },
        child: SingleChildScrollView(
          child: Column(
            children: <Widget>[
              Stack(
                children: <Widget>[
                  Hero(
                    tag: "firstClipp",
                    child: ClipShadow(
                      clipper: PrimaryClipper(),
                      boxShadow: [
                        BoxShadow(
                            color: Colors.black12,
                            blurRadius: 10,
                            spreadRadius: 10,
                            offset: Offset(0.0, 1.0))
                      ],
                      child: Container(
                        width: MediaQuery.of(context).size.width,
                        height: (MediaQuery.of(context).size.height * (1 / 4)),
                        color: accent,
                      ),
                    ),
                  ),
                  Hero(
                    tag: "secondClipp",
                    child: ClipShadow(
                      clipper: SecondClipper(),
                      boxShadow: [
                        BoxShadow(
                            color: Colors.black12,
                            blurRadius: 10,
                            spreadRadius: 10,
                            offset: Offset(0.0, 1.0))
                      ],
                      child: Container(
                        width: MediaQuery.of(context).size.width,
                        height: (MediaQuery.of(context).size.height * (1 / 4)),
                        child: Container(
                          color: base.withOpacity(0.7),
                        ),
                      ),
                    ),
                  ),
                  Container(
                    height: MediaQuery.of(context).size.height / 4,
                    child: Center(
                      child: MyText(
                        "Définir itinéraire",
                        color: white,
                        fontSize: 50,
                      ),
                    ),
                  ),
                  Positioned(
                    top: 40,
                    right: 10,
                    child: MaterialButton(
                      shape: CircleBorder(),
                      child: Icon(
                        Icons.close,
                        color: accent,
                      ),
                      color: Colors.white,
                      onPressed: () {
                        Navigator.of(context).pop();
                      },
                    ),
                  ),
                ],
              ),
              Center(
                child: Container(
                  width: MediaQuery.of(context).size.width * 0.8,
                  height: MediaQuery.of(context).size.height / 2,
                  child: Form(
                    key: _formKey,
                    child: Column(
                      children: <Widget>[
                        Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: <Widget>[
                            Expanded(
                              child: PaddingWith(
                                  top: 30,
                                  bottom: 15,
                                  widget: MyFormTextField(
                                      controller: start,
                                      validator: validatorAdress,
                                      hint: "Entrez votre adresse de départ",
                                      labelText: 'Départ',
                                      icon: Icons.place)),
                            ),
                            PaddingWith(
                                right: 15,
                                widget: Container(
                                  decoration: const ShapeDecoration(
                                    color: accent,
                                    shape: CircleBorder(),
                                  ),
                                  child: MyIconButton(
                                    function: () {
                                      localize();
                                    },
                                    icon: Icon(Icons.my_location),
                                  ),
                                )),
                          ],
                        ),
                        PaddingWith(
                            top: 15,
                            bottom: 30,
                            widget: MyFormTextField(
                                controller: finish,
                                validator: validatorAdress,
                                hint: "Entrez votre adresse d'arrivée",
                                labelText: 'Arrivée',
                                maxLines: 1,
                                icon: Icons.place)),
                        Expanded(

                          child :GridView.count(
                            crossAxisCount: 5,
                            children:icons.map((widget) {
                              final index = ++counter - 1;

                              return ToggleButtons(
                                splashColor: base,
                                hoverColor: accent,
                                selectedColor: Colors.green,
                                color: Colors.red,
                                isSelected: [selections[index]],
                                renderBorder: false,
                                onPressed: (_) => {
                                  onControlPress(index)
                                },
                                children: [widget],
                              );
                            }).toList()),),

                      ],
                    ),
                  ),
                ),
              ),
              PaddingWith(
                top: 20,
                widget : ToggleButtons(
                splashColor: base,
                hoverColor: accent,
                selectedColor: Colors.green,
                color: Colors.red,
                isSelected: [vert],
                renderBorder: false,
                onPressed: (_) => {
                  this.setState(() {
                    this.vert=!vert;
                  })
                },
                children: [Icon(Icons.public)],
              ),),
              PaddingWith(
                top: 20,
                widget: SizedBox(
                  height: 40,
                  width: MediaQuery.of(context).size.width / 1.5,
                  child: MyButton(
                    function: () async {
                      if (_formKey.currentState.validate()) {
                        getJourney();
                      }
                    },
                    color: accent,
                    textColor: white,
                    borderColor: base,
                    name: "Valider",
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  void onControlPress(index){
    if(selections[index]){
      filters.add(transports[index]);
    }
    else{
      filters.remove(transports[index]);
    }
    this.setState(() {
      selections[index]=!selections[index];

    });

  }


  void localize()async {

    Future<Position> myPos = Geolocator()
        .getLastKnownPosition(desiredAccuracy: LocationAccuracy.high);
    myPos.then((value) => {
          if (value != null){
            position = value,
            start.text = position.toString()
            }
        });
  }



  bool hasTransportAvailable(){
    for(bool rep in selections){
      if(rep==true){
        return true;
      }
    }
    return false;
  }

  void getJourney() async {
    

    Pos startPos;
    Pos endPos;

    if (start.text != position.toString()) {
      List<Location> locationsStart =
          await MapHelper().getCoordonateFromAdress(start.text);
      if (locationsStart == null) {
        startPos =null;
      }
      else {
        startPos =
        new Pos(locationsStart[0].latitude, locationsStart[0].longitude);
      }
    } else {
      startPos = new Pos(position.latitude, position.longitude);
    }
    List<Location> locationsFinish =
        await MapHelper().getCoordonateFromAdress(finish.text);
    if (locationsFinish == null) {
      endPos = null;
    }
    else {
      endPos =
      new Pos(locationsFinish[0].latitude, locationsFinish[0].longitude);
    }
    print(startPos);
    print(endPos);
    SharedPreferences prefs = await SharedPreferences.getInstance();
    int id = prefs.getInt("id");
    if(id==null){
      id=1;
    }
    JourneyHelper().getJourneyInfo(startPos, endPos, id, filters, vert).then((value) => {
          if (value != null)
            {
              Navigator.push(
                  context,
                  MaterialPageRoute(
                      builder: (context) => JourneyPage(journey: value, filters: filters, green: vert,)))
            }
          else
            {AlertHelper().error(context, "Erreur", "Aucun trajet trouvé ! Essayez d'élargir vos filtres")}
        });
  }

  String validatorAdress(value) {
    if (value.isEmpty) {
      return 'Merci de remplir ce champ';
    }
    if (value.length > 300) {
      return "adresse trop longue";
    }
    return null;
  }

  void hideKeyBoard() {
    FocusScope.of(context).requestFocus(new FocusNode());
  }
}
