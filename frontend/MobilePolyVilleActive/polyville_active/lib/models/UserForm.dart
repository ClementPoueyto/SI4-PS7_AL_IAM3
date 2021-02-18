import 'package:polyvilleactive/models/ModeTransport.dart';
class UserForm {

  List<ModeTransport> filters;
  bool green;
  UserForm(List<ModeTransport> filters, bool green) {
    this.green=green;
    this.filters=filters;
  }

  objectToJson(){
    List<String> filtersToSend=new List();
    filters.forEach((element) {
      filtersToSend.add(element.value);
    });
    return toJson(filtersToSend);
  }

  Map<String, dynamic> toJson(filters) =>
      {
        'filters': filters,
        'green':green
      };

}