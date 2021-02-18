

class ResponseDemands {

  bool accepted = false;
  int idDemands;

  Map<String, dynamic> toJson() =>
      {
        'accepted': this.accepted,
        'idDemands': this.idDemands,
      };
  ResponseDemands(int idDemands, bool accepted) {
    this.idDemands=idDemands;
    this.accepted=accepted;
  }

}