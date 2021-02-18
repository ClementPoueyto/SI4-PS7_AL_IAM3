import 'package:clip_shadow/clip_shadow.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:polyvilleactive/controller/form_controller.dart';
import 'package:polyvilleactive/my_material.dart';
import 'package:polyvilleactive/view/page/profile_page.dart';
import 'package:shared_preferences/shared_preferences.dart';
class MenuPage extends StatelessWidget{
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      endDrawer: MyDrawer(context: context,),
      appBar: AppBar(
        leading: SizedBox.shrink(),
      ),
      backgroundColor: white,
      body: Column(
        children: <Widget>[
          Stack(
            children: <Widget>[
              ClipShadow(
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
              ClipShadow(
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
              Container(
                height: MediaQuery.of(context).size.height / 4,
                child: Center(
                  child: MyText(
                    "Menu",
                    color: white,
                    fontSize: 55,
                  ),
                ),
              ),

            ],
          ),
          PaddingWith(
            top: 20,
            widget: Hero(
              tag: "form",
              child: SizedBox(
                  height: 40,
                  width: MediaQuery.of(context).size.width / 2,
                  child: MyButton(
                    name: "Recherche de trajet",
                    color: whiteShadow,
                    borderColor: base,
                    function: () {
                      Navigator.push(
                          context,
                          MaterialPageRoute(
                              builder: (context) => FormController()));
                    },
                  )),
            ),
          ),

        ],
      ),
    );
  }

  Future<void> logout(BuildContext context) async {
    SharedPreferences prefs = await SharedPreferences.getInstance();
    int id = prefs.getInt('id');
    if (id != null) {
      prefs.clear();
    }
    Navigator.of(context).pop();
  }
}