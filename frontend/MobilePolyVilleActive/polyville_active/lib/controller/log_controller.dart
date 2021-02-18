import 'package:clip_shadow/clip_shadow.dart';
import 'package:flutter/material.dart';
import 'package:polyvilleactive/controller/form_controller.dart';
import 'package:polyvilleactive/controller/log_in_controller.dart';
import 'package:polyvilleactive/controller/register_controller.dart';
import 'package:polyvilleactive/my_material.dart';
import 'package:polyvilleactive/ui/my_clipper.dart';
import 'package:polyvilleactive/ui/my_second_clipper.dart';
import 'package:polyvilleactive/view/page/menu_page.dart';
import 'package:shared_preferences/shared_preferences.dart';

///Page d'accueil de l'application
class LogController extends StatefulWidget {
  _LogState createState() => _LogState();
}

class _LogState extends State<LogController> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: NotificationListener<OverscrollIndicatorNotification>(
      // ignore: missing_return
      onNotification: (overscroll) {
        overscroll.disallowGlow();
      },
      child: SingleChildScrollView(
          child: Column(children: <Widget>[
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
                  height: (MediaQuery.of(context).size.height * (1 / 2)),
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
                  height: (MediaQuery.of(context).size.height * (1 / 2)),
                  child: Container(
                    color: base.withOpacity(0.7),
                  ),
                ),
              ),
            ),
            Container(
                height: MediaQuery.of(context).size.height / 2,
                child: Center(
                  child: MyText(
                    "PolyVilleActive",
                    color: white,
                    fontSize: 60,
                  ),
                )),
          ],
        ),
        Container(
          height: (MediaQuery.of(context).size.height * (1 / 2)),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[

              PaddingWith(
                bottom: 20,
                widget: Hero(
                  tag: "loginButton",
                  child: SizedBox(
                    height: 40,
                    width: MediaQuery.of(context).size.width / 1.5,
                    child: MyButton(
                      name: "Se connecter",
                      textColor: white,
                      color: accent,
                      borderColor: base,
                      function: () {
                        isConnected();
                      },
                    ),
                  ),
                ),
              ),
              PaddingWith(
                top: 20,
                widget: Hero(
                  tag: "registerButton",
                  child: SizedBox(
                      height: 40,
                      width: MediaQuery.of(context).size.width / 2,
                      child: MyButton(
                        name: "Créer un compte",
                        color: whiteShadow,
                        borderColor: base,
                        function: () {
                          Navigator.push(
                              context,
                              MaterialPageRoute(
                                  builder: (context) => RegisterController()));
                        },
                      )),
                ),
              ),
            ],
          ),
        )
      ])),
    ));
  }


  void isConnected() async{
    SharedPreferences prefs = await SharedPreferences.getInstance();
    int id =prefs.getInt('id');
    if(id==null){
      Navigator.push(
          context,
          MaterialPageRoute(
              builder: (context) => LogInController()));
    }
    else{
      Navigator.push(
          context,
          MaterialPageRoute(
              builder: (context) => MenuPage()));
    }

  }
}
