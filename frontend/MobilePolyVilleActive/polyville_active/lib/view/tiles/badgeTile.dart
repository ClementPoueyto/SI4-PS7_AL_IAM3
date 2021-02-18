import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:polyvilleactive/models/Badge.dart';

class BadgeTile extends StatelessWidget{
  final Badge badge;
  BadgeTile({this.badge});

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Card(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            ListTile(
              title:Image(
          width: 100,
          height: 100,
          image: AssetImage("assets/badge/"+badge.image),
        ) ,
              subtitle: Center(child :Text(badge.name)),
            ),

          ],
        ),
      ),
    );
  }
}
