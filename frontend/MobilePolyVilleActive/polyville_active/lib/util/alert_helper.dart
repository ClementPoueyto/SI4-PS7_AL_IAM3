import 'package:flutter/material.dart';
import 'package:flutter/cupertino.dart';
import 'package:polyvilleactive/util/api_user_helper.dart';


/// Classe permettant d'ouvrir des boites de dialogues afin d'informer l'utilisateur
enum ConfirmAction { CANCEL, ACCEPT }

class Dialog_button_ask_handler {
  Future<String> accept() async{}
  Future<String> refuse()async{}
}

class AlertHelper {

  ///ALERT ERROR
  Future<void> error(
      BuildContext context, String errorTitle, String error) async {
    Text title = Text(errorTitle);
    Text subtitle = Text(error);
    return showDialog(
      context: context,
      barrierDismissible: true,  // user don't need to tap button for close dialog!
      builder: (BuildContext ctx) {
        return (Theme.of(context).platform == TargetPlatform.iOS)
            ? CupertinoAlertDialog(
                title: title,
                content: subtitle,
                actions: <Widget>[close(ctx, "OK")])
            : AlertDialog(
                title: title,
                content: subtitle,
                actions: <Widget>[close(ctx, "OK")]);
      },
    );
  }

  Future<void> ask(
      BuildContext context,Dialog_button_ask_handler dialog_button_ask_handler,
      String errorTitle, String error) async {
    Text title = Text(errorTitle);
    Text subtitle = Text(error);
    return showDialog(
      context: context,
      barrierDismissible: true,  // user don't need to tap button for close dialog!
      builder: (BuildContext ctx) {
        return (Theme.of(context).platform == TargetPlatform.iOS)
            ? CupertinoAlertDialog(
            title: title,
            content: subtitle,
            actions: <Widget>[close(ctx, "OK")])
            : AlertDialog(
            title: title,
            content: subtitle,
            actions: <Widget>[accept(ctx,dialog_button_ask_handler, "YES"),refuse(ctx,dialog_button_ask_handler, "NO")]);
      },
    );
  }
  /// bouton Close Alert error
  FlatButton close(BuildContext ctx, String text) {
    return FlatButton(
      onPressed: (() => Navigator.pop(ctx,ConfirmAction.CANCEL)),
      child: Text(
        text,
      ),
    );
  }

  /// Button accept
  FlatButton accept(BuildContext ctx, Dialog_button_ask_handler dialog_button_ask_handler,String text) {
    return FlatButton(
      onPressed: (() =>{
        dialog_button_ask_handler.accept(),
        Navigator.pop(ctx,ConfirmAction.CANCEL)
      }),
      child: Text(
        text,
      ),
    );
  }

  /// Button refuse
  FlatButton refuse(BuildContext ctx,Dialog_button_ask_handler dialog_button_ask_handler, String text) {
    return FlatButton(
      onPressed: (() => {
        dialog_button_ask_handler.refuse(),
        Navigator.pop(ctx,ConfirmAction.CANCEL)
      }),
      child: Text(
        text,
      ),
    );
  }

  ///ALERTE DE DECONNECTION
  Future<void> logOut(
      BuildContext context, String titleDialog, String subtitleDialog) async {
    Text title = Text(titleDialog);
    Text subtitle = Text(subtitleDialog);
    return showDialog(
        context: context,
        barrierDismissible: true,
        builder: (BuildContext ctx) {
          return (Theme.of(context).platform == TargetPlatform.iOS)
              ? CupertinoAlertDialog(
              title: title,
              content: subtitle,
              actions: <Widget>[
                close(ctx, "Annuler"),
                logOutBtn(ctx, "Se déconnecter")
              ])
              : AlertDialog(title: title, content: subtitle, actions: <Widget>[
            close(ctx, "Annuler"),
            logOutBtn(ctx, "Se déconnecter")
          ]);
        });
  }

  ///Bouton validation de deconnection
  FlatButton logOutBtn(BuildContext ctx, String text) {
    return FlatButton(
      onPressed: (() => {ApiUserHelper().logOut(ctx)}),
      child: Text(
        text,
      ),
    );
  }
}
