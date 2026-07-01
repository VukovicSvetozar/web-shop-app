export class PretragaZahtjev {

  kategorijaId: number;
  korisnickoIme: string;
  kratakNaslov: string;
  lokacija: string;
  minCijena: number;
  maxCijena: number;
  datumOd: string;
  datumDo: string;
  polovan: boolean;
  nov: boolean;

  constructor(
    kategorijaId: number,
    korisnickoIme: string,
    kratakNaslov: string,
    lokacija: string,
    minCijena: number,
    maxCijena: number,
    datumOd: string,
    datumDo: string,
    polovan: boolean,
    nov: boolean
  ) {
    this.kategorijaId = kategorijaId;
    this.korisnickoIme = korisnickoIme;
    this.kratakNaslov = kratakNaslov;
    this.lokacija = lokacija;
    this.minCijena = minCijena;
    this.maxCijena = maxCijena;
    this.datumOd = datumOd
    this.datumDo = datumDo;
    this.polovan = polovan;
    this.nov = nov;
  }

}
