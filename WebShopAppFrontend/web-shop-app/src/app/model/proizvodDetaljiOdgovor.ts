export class ProizvodDetaljiOdgovor {

  avatarUrl: string;
  korisnickoIme: string;
  datumPristupa: string;
  grad: string;
  email: string;
  brojTelefona: string;
  vrijemeObjave: string;
  kratakNaslov: string;
  detaljanOpis: string;
  cijena: number;
  polovan: boolean;
  lokacija: string;
  nazivKategorije: string;
  specificneOsobine: { [key: string]: string };
  slikeProizvodaUrl: string[];

  constructor(avatarUrl: string,
    korisnickoIme: string,
    datumPristupa: string,
    grad: string,
    email: string,
    brojTelefona: string,
    vrijemeObjave: string,
    kratakNaslov: string,
    detaljanOpis: string,
    cijena: number,
    polovan: boolean,
    lokacija: string,
    nazivKategorije: string,
    specificneOsobine: { [key: string]: string },
    slikeProizvodaUrl: string[]) {
    this.avatarUrl = avatarUrl;
    this.korisnickoIme = korisnickoIme;
    this.datumPristupa = datumPristupa;
    this.grad = grad;
    this.email = email;
    this.brojTelefona = brojTelefona;
    this.vrijemeObjave = vrijemeObjave;
    this.kratakNaslov = kratakNaslov;
    this.detaljanOpis = detaljanOpis;
    this.cijena = cijena;
    this.polovan = polovan;
    this.lokacija = lokacija;
    this.nazivKategorije = nazivKategorije;
    this.specificneOsobine = specificneOsobine;
    this.slikeProizvodaUrl = slikeProizvodaUrl;
  }

}
