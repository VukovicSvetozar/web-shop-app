export class ProizvodZahtjev {

  kratakNaslov: string;
  detaljanOpis: string;
  cijena: number;
  polovan: boolean;
  lokacija: string;
  kategorijaId: number;
  korisnickoIme: string

  constructor(
    kratakNaslov: string,
    detaljanOpis: string,
    cijena: number,
    polovan: boolean,
    lokacija: string,
    kategorijaId: number,
    korisnickoIme: string
  ) {
    this.kratakNaslov = kratakNaslov;
    this.detaljanOpis = detaljanOpis;
    this.cijena = cijena;
    this.polovan = polovan;
    this.lokacija = lokacija;
    this.kategorijaId = kategorijaId
    this.korisnickoIme = korisnickoIme;
  }

}
