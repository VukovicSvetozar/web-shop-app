export class ProizvodKarticaOdgovor {

  id: number;
  kratakNaslov: string;
  cijena: number;
  nazivKategorije: string;
  posterUrl: string;

  constructor(
    id: number,
    kratakNaslov: string,
    cijena: number,
    nazivKategorije: string,
    posterUrl: string
  ) {
    this.id = id;
    this.kratakNaslov = kratakNaslov;
    this.cijena = cijena;
    this.nazivKategorije = nazivKategorije;
    this.posterUrl = posterUrl;
  }

}
