import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:polyvilleactive/models/Reward.dart';
import 'package:polyvilleactive/models/user.dart';
import 'package:polyvilleactive/my_material.dart';
import 'package:polyvilleactive/util/alert_helper.dart';
import 'package:polyvilleactive/util/reward_helper.dart';

class RewardTile extends StatelessWidget{
  final Reward reward;
  final User user;
  final Function function;
  RewardTile({this.reward, this.user, this.function});

  @override
  Widget build(BuildContext context) {
    return Container(
      height: 100,

      child :Card(
      color: user.points<reward.points?Colors.grey:white,
      child: FlatButton(
        onPressed: (){onSelectGift(reward,user,context);},
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: <Widget>[
            ListTile(
              leading: Icon(Icons.card_giftcard),
              title:Center(child :Text(reward.points.toString()+" points")),
              subtitle: Center(child :Text(reward.title)),
            ),

          ],
        ),
      ),),
    );
  }

  onSelectGift(Reward reward, User user,BuildContext context){
    if(user.points<reward.points){
      AlertHelper().error(context, "Points insuffisants", "Vous n'avez pas encore assez de points pour débloquer ce cadeau");
    }
    else{
      RewardHelper().spendPoints(context,user.uid, reward.id).then((value) => {
        if(value!=null){
          AlertHelper().error(context, "Felicitation !", "Voici votre code : "+value.toString()),
          this.function(this.user.points=this.user.points-reward.points),
        }
      });
    }
  }
}
