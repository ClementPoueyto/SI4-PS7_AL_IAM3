import 'package:clip_shadow/clip_shadow.dart';
import 'package:flutter/material.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:polyvilleactive/my_material.dart';
import 'package:polyvilleactive/util/alert_helper.dart';
import 'package:polyvilleactive/util/api_user_helper.dart';

/// Page d'enregistrement du compte
class RegisterController extends StatefulWidget {
  _RegisterState createState() => _RegisterState();
}

class _RegisterState extends State<RegisterController> {
  TextEditingController _username;
  TextEditingController _mail;
  TextEditingController _pwd;
  TextEditingController _pwd2;

  final _formKey = GlobalKey<FormState>();

  @override
  void initState() {
    super.initState();
    _mail = TextEditingController();
    _pwd = TextEditingController();
    _pwd2 = TextEditingController();
    _username = TextEditingController();
  }

  @override
  void dispose() {
    super.dispose();
    _mail.dispose();
    _pwd.dispose();
    _pwd2.dispose();
    _username.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: new GestureDetector(
        onTap: () {
          hideKeyBoard();
        },
        child: SingleChildScrollView(
          child: Center(
            child: Column(
              children: <Widget>[
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
                          height:
                              (MediaQuery.of(context).size.height * (1 / 5)),
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
                          height:
                              (MediaQuery.of(context).size.height * (1 / 5)),
                          child: Container(
                            color: base.withOpacity(0.7),
                          ),
                        ),
                      ),
                    ),
                    Container(
                        height: MediaQuery.of(context).size.height / 5,
                        child: Center(
                          child: MyText(
                            "S'enregistrer",
                            color: white,
                            fontSize: 40,
                          ),
                        )),
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
                Form(
                  key: _formKey,
                  child: Container(
                    width: MediaQuery.of(context).size.width * 0.8,
                    child: Column(
                      children: <Widget>[

                        PaddingWith(
                            top: 15,
                            bottom: 15,
                            widget: MyFormTextField(
                                validator: validatorName,
                                controller: _username,
                                hint: "Entrez votre pseudo",
                                labelText: 'Pseudo',
                                icon: Icons.person)),
                        PaddingWith(
                            top: 15,
                            bottom: 15,
                            widget: MyFormTextField(
                                validator: validatorMail,
                                controller: _mail,
                                hint: "Entrez votre adresse mail",
                                labelText: 'Mail',
                                icon: Icons.mail)),

                        PaddingWith(
                            top: 15,
                            bottom: 15,
                            widget: MyFormTextField(
                                validator: validatorPwd,
                                controller: _pwd,
                                hint: "Entrez votre mot de passe",
                                obscure: true,
                                maxLines: 1,
                                labelText: 'Mot de passe',
                                icon: Icons.lock)),
                        PaddingWith(
                          top: 15,
                          bottom: 30,
                          widget: MyFormTextField(
                              validator: validatorPwd,
                              controller: _pwd2,
                              hint: "Répétez votre mot de passe",
                              obscure: true,
                              maxLines: 1,
                              labelText: 'Confirmer le mot de passe',
                              icon: Icons.lock),
                        ),
                        PaddingWith(
                          widget: Hero(
                            tag: "registerButton",
                            child: SizedBox(
                              height: 40,
                              width: MediaQuery.of(context).size.width / 1.5,
                              child: MyButton(
                                name: "Créer un compte",
                                textColor: white,
                                color: accent,
                                borderColor: base,
                                function: () {
                                  if (_formKey.currentState.validate())
                                    signIn();
                                },
                              ),
                            ),
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  void signIn() async {
    if (_mail.text != null &&
        _mail.text != "" &&
        _pwd.text != null &&
        _pwd.text != "" &&
        _username.text != null &&
        _username.text != "") {
      ApiUserHelper()
          .signIn(_mail.text.trim(), _pwd.text, _username.text, context)
          .then((value) => {
                print(value.toString()),
                {
                  Fluttertoast.showToast(
                      msg: "Compte créé avec succès",
                      toastLength: Toast.LENGTH_SHORT,
                      gravity: ToastGravity.BOTTOM,
                      timeInSecForIosWeb: 1,
                      fontSize: 16.0),
                  Navigator.pop(context)
                }
              })
          .catchError((e) {
        AlertHelper().error(context, "Erreur", manageErrors(e));
      });
    }
  }

  String validatorName(String value) {
    if (value.isEmpty) {
      return 'Merci de remplir ce champ';
    }
    if (value.length > 100) {
      return "champ trop long";
    }

    return null;
  }

  String validatorPseudo(String value) {
    if (value.isEmpty) {
      return 'Merci de remplir ce champ';
    }
    if (value.contains(" ")) {
      return "Espace non autorisé dans le pseudo";
    }
    if (value.length > 20) {
      return "champ trop long";
    }

    return null;
  }

  String validatorMail(String value) {
    if (value.isEmpty) {
      return 'Merci de remplir ce champ';
    }
    if (!value.contains("@")) {
      return 'Adresse mail non valide';
    }
    if (value.length > 100) {
      return "mail trop long";
    }
    return null;
  }

  String validatorPwd(value) {
    if (value.isEmpty) {
      return 'Merci de remplir ce champ';
    }
    if (value.length < 3) {
      return 'Le mot de passe doit contenir au moins 3 caractères';
    }
    if (value.length > 100) {
      return "mot de passe trop long";
    }
    if (_pwd.text != _pwd2.text) {
      return "mots de passe differents";
    }
    return null;
  }

  void hideKeyBoard() {
    FocusScope.of(context).requestFocus(new FocusNode());
  }

  String manageErrors(String error) {
    switch (error) {
      case "user already exist":
        return "Désolé, cet utilisateur existe déjà";
      case "pseudo already exist":
        return "Désolé ce pseudo est déjà utilisé";
      case "wrong email":
        return "Merci de remplir une adresse email valide";
      case "missing parameters":
        return "Merci de remplir tous les champs";
      case "wrong pseudo (must be length 4 - 20)":
        return "Mauvais pseudo, celui-ci doit faire entre 4 et 20 caractères";
      case "wrong first name (must be length 1 - 20)":
        return "Mauvais prénom, celui-ci doit faire entre 1 et 20 caractères";
      case "wrong last name (must be length 1 - 20)":
        return "Mauvais nom, celui-ci doit faire entre 1 et 20 caractères";
      case "error server":
        return "Oops. Une erreur s'est produite, veuillez rééssayer plus tard";
      default:
        return "Une erreur s'est produite, veuillez verifier votre connexion internet";
    }
  }
}
