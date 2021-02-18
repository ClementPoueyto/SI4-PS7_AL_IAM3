import 'package:flutter/material.dart';
import 'package:polyvilleactive/models/ModeTransport.dart';
import 'package:polyvilleactive/models/Section.dart';
import 'package:polyvilleactive/my_material.dart';

class SectionTile extends StatelessWidget{
  final Section section;
  SectionTile({this.section});

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Card(
        color: section.reached?Colors.lightGreen:white,
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            ListTile(

              leading: section.modeTransport.icon,
              title: MyText(section.from!=null?"Départ de "+section.from.address:"Attente"),
              subtitle: Text("à "+section.departureDateTime.hour.toString()+":"+(section.departureDateTime.minute<10?"0"+section.departureDateTime.minute.toString():section.departureDateTime.minute.toString())
                  +" en "+section.modeTransport.translate+ (section.line!=""?" \nligne : "+section.line:"")),
            ),
            Column(
              mainAxisAlignment: MainAxisAlignment.end,
              children: <Widget>[
                Text("Arrivée à "+section.arrivalDateTime.hour.toString()+":"
                    +(section.arrivalDateTime.minute<10?"0"+section.arrivalDateTime.minute.toString():section.arrivalDateTime.minute.toString())),

                const SizedBox(height: 8),
                section.to!=null?Text("à "+section.to.address.toString()):SizedBox.shrink(),
                const SizedBox(height: 8),

              ],
            ),
          ],
        ),
      ),
    );
  }
  }


/*
Container(
        width: MediaQuery.of(context).size.width,
        height: 200,
    margin: EdgeInsets.all(5.0),
    child: Card(
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(20.0),
      ),

      elevation: 10,
      child: Row(
        children: <Widget>[
          section.from!=null&&section.from.address!=null?
          MyText("Départ de "+section.from.address):SizedBox.shrink(),
          MyText("à "+section.departureDateTime.hour.toString()+":"+section.departureDateTime.minute.toString())


        ],
      )
    )
    );
 */