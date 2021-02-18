import 'package:flutter/cupertino.dart';
import 'package:polyvilleactive/models/Reward.dart';
import 'package:polyvilleactive/my_material.dart';
import 'package:polyvilleactive/util/alert_helper.dart';
import 'constants.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:flutter/material.dart';

class RewardHelper {
  Future<List<Reward>> getRewards(BuildContext context) async {
    List<Reward> rewards=new List();
    String errorMessage;
    try {
      final result = (await http.get(urlApi + "rewards",
          headers: {
            "Accept": "application/json",
            'Content-Type': 'application/json; charset=UTF-8',
          }
      ));
      if(result.statusCode==200) {
        List<dynamic> rewardsJson = json.decode(utf8.decode(result.bodyBytes));
        if(rewardsJson.length>0){
          for(Map<String,dynamic> map in rewardsJson){
            rewards.add(new Reward(map));
          }
        }
      }
      else{
        errorMessage=result.body;
      }
    } catch (error) {
      errorMessage = error.toString();
    }
    if (errorMessage != null) {
      AlertHelper().error(context, "Erreur serveur", errorMessage);
      return Future.error(errorMessage);
    }
    return rewards;
  }

  Future<String> spendPoints(BuildContext context,int id, int rid) async {
    String code;
    String errorMessage;
    try {
      final result = (await http.get(urlApi + "spendpoints?uid="+id.toString()+"&rid="+rid.toString(),
          headers: {
            "Accept": "application/json",
            'Content-Type': 'application/json; charset=UTF-8',
          }
      ));
      if(result.statusCode==200) {
        code = (result.body);

      }
      else{
        errorMessage=result.body;
      }
    } catch (error) {
      errorMessage = error.toString();
    }
    if (errorMessage != null) {
      AlertHelper().error(context, "Erreur serveur", errorMessage);
      return Future.error(errorMessage);
    }
    return code;
  }
}
