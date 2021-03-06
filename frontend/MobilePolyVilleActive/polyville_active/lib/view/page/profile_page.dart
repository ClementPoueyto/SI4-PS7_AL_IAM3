import 'package:clip_shadow/clip_shadow.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:polyvilleactive/models/user.dart';
import 'package:polyvilleactive/my_material.dart';
import 'package:polyvilleactive/util/api_user_helper.dart';
import 'package:polyvilleactive/view/tiles/badgeTile.dart';
import 'package:shared_preferences/shared_preferences.dart';
class ProfilePage extends StatefulWidget{
  _ProfileState createState() => _ProfileState();

}

class _ProfileState extends State<ProfilePage> {


  User user;

  @override
  void initState() {
    super.initState();
    Future<SharedPreferences> prefs =  SharedPreferences.getInstance();
    prefs.then((value) => {
      ApiUserHelper().getUser(value.getInt('id'), context).then((value) =>
        this.setState(() {
          if(value!=null) {
            print(value.uid);
            this.setState(() {
              user = value;
            });
          }})
      )

    });

  }

  @override
  void dispose() {
    super.dispose();
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(

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
                  height: (MediaQuery.of(context).size.height * (1 / 5)),
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
                  height: (MediaQuery.of(context).size.height * (1 / 5)),
                  child: Container(
                    color: base.withOpacity(0.7),
                  ),
                ),
              ),
              Container(
                height: MediaQuery.of(context).size.height / 5,
                child: Center(
                  child: MyText(
                    "Profil",
                    color: white,
                    fontSize: 55,
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
                    Navigator.pop(context);
                  },
                ),
              ),
            ],
          ),
          user!=null?
          Container(
            height: 30,
            child: Center(
              child: MyText(
                user!=null?"Pseudo : "+user.username:"",
                color: black,
                fontSize: 20,
              ),
            ),
          ):SizedBox.shrink(),
          user!=null?Container(
            height: 30,
            child: Center(
              child: MyText(
                user!=null?"Email : "+user.email:"",
                color: black,
                fontSize: 20,
              ),
            ),
          ):SizedBox.shrink(),
          PaddingWith(
            top: 20,
            widget :Image(
            width: 100,
            height: 100,
            image: AssetImage("assets/icon/profile.png"),
          ) ,),
          user!=null?Container(
            height: 30,
            child: Center(
              child: MyText(
                user!=null?"Score : "+user.score.toString():"",
                color: black,
                fontSize: 20,
              ),
            ),
          ):SizedBox.shrink(),
          user!=null?Container(
            height: 30,
            child: Center(
              child: MyText(
                user!=null?"Points : "+user.points.toString():"",
                color: black,
                fontSize: 20,
              ),
            ),
          ):SizedBox.shrink(),
          user!=null&&user.badges.length>0?Expanded(child :
          GridView.count(
            // Create a grid with 2 columns. If you change the scrollDirection to
            // horizontal, this produces 2 rows.
            crossAxisCount: 2,
            // Generate 100 widgets that display their index in the List.
            children: List.generate(user.badges.length, (index) {
              return Center(
                child: BadgeTile(badge: user.badges[index],)
              );
            }),
            ),
          ):
          PaddingWith(
            widget: MyText("Vous n'avez pas encore débloqué de badges"),
          ),

        ],
      ),
    );
  }


}