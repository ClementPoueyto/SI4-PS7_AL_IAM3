import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:polyvilleactive/main.dart';
import 'package:polyvilleactive/models/user.dart';
import 'package:polyvilleactive/util/alert_helper.dart';
import 'package:polyvilleactive/my_material.dart';
import 'package:shared_preferences/shared_preferences.dart';
///Appel API USER

class ApiUserHelper {

  ///Permet de s'inscrire et Renvoie un User
  Future<User> signIn(String mail, String pwd, String pseudo, BuildContext context) async {
    User user;
    String errorMessage;
    try {
      final result = (await http.post(urlApi + "signin",
          headers: {
            "Accept": "application/json",
            'Content-Type': 'application/json; charset=UTF-8'
          },
          body: jsonEncode(<String, String>{
            'email': mail,
            'password': pwd,
            'username': pseudo,
          })));
      if(result.statusCode==201){
        return User(jsonDecode(result.body));
      }
      else if(result.statusCode==500){
        return Future.error("error server");
      }
      else{
        return Future.error(jsonDecode(result.body)['error']);
      }
    } catch (error) {
      errorMessage = error.toString();
      print(errorMessage.toString());
    }
    if (errorMessage != null) {
      AlertHelper().error(context, "Erreur d'authentification", errorMessage);
      return Future.error(errorMessage);
    }

    return user;
  }

  Future<User> logIn(String username, String pwd, BuildContext context) async {
    User user;
    String errorMessage;
    try {
      final result = (await http.post(urlApi + "login",
          headers: {
            "Accept": "application/json",
            'Content-Type': 'application/json; charset=UTF-8'
          },
          body: jsonEncode(<String, String>{
            'username': username,
            'password': pwd,
          })));
      user = User(jsonDecode(result.body));
    } catch (error) {
      errorMessage = error.toString();
    }
    if (errorMessage != null) {
      AlertHelper().error(context, "Erreur d'authentification", errorMessage);
      return Future.error(errorMessage);
    }
    return user;
  }

  Future<User> getUser(int id, BuildContext context) async {
    User user;
    String errorMessage;
    try {
      final result = (await http.get(urlApi + "user?id="+id.toString(),
          headers: {
            "Accept": "application/json",
            'Content-Type': 'application/json; charset=UTF-8',
          }
          ));
      if(result.statusCode==200) {
        user = User(jsonDecode(result.body));
      }
      else{
        errorMessage=result.body;
      }
    } catch (error) {
      errorMessage = error.toString();
    }
    if (errorMessage != null) {
      AlertHelper().error(context, "Erreur d'authentification", errorMessage);
      return Future.error(errorMessage);
    }
    return user;
  }

  ///Permet de se deconnecter, supprime le token de connection, reinitialise les données partagées
  Future logOut(BuildContext context) async {
    Future<SharedPreferences> prefs =  SharedPreferences.getInstance();
    prefs.then((value) => {
    Navigator.of(context).pop(),
      Navigator.of(context).pop(),
      value.clear()

    });
  }
}
