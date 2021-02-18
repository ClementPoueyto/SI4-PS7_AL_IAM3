import 'package:flutter/material.dart';
import 'package:polyvilleactive/models/user.dart';
import 'package:polyvilleactive/my_material.dart';
import 'package:polyvilleactive/util/alert_helper.dart';
import 'package:polyvilleactive/util/api_user_helper.dart';
import 'package:polyvilleactive/util/constants.dart';
import 'package:polyvilleactive/view/page/gift_page.dart';
import 'package:polyvilleactive/view/page/menu_page.dart';
import 'package:polyvilleactive/view/page/profile_page.dart';


class MyDrawer extends Drawer {
  MyDrawer({
    @required BuildContext context,
    User user,
    String name,
    Color color,
    Color borderColor: Colors.grey,
    Color textColor: black,
  }) : super(
          child: Column(
            children: <Widget>[
              DrawerHeader(
                decoration: BoxDecoration(
                  color: base,
                  image: DecorationImage(
                      fit: BoxFit.cover,
                    image: AssetImage("assets/icon/profile.png")
                  ),
                ),
              ),
              ListTile(
                leading: Icon(Icons.person),
                title: MyText("Mon profil",color: black,),
                onTap: (){
                  Navigator.push(context,
                      MaterialPageRoute(builder: (context) => ProfilePage()));
                },
              ),
              ListTile(
                leading: Icon(Icons.card_giftcard),
                title: MyText("Mes récompenses",color: black,),
                onTap: (){
                  Navigator.push(context,
                      MaterialPageRoute(builder: (context) => GiftPage()));
                },
              ),
              Divider(),
              ListTile(
                leading: Icon(Icons.error),
                title: MyText("Plus d'infos",color: black,),
                onTap: (){
                  showAboutDialog(context: context,
                    applicationName: "PolyVilleActive",
                    applicationVersion: "1.0"
                  );
                },
              ),
              Divider(),
              Expanded(child: SizedBox(),),
              ListTile(
                leading: Icon(Icons.arrow_back),
                title: MyText("Se deconnecter", color: Colors.red,),
                onTap: (){
                  ApiUserHelper().logOut(context);
                },
              )

            ],
          ),
        );
}
