export class PorukaPodrskeZahtjev {

  naslovPoruke: string;
  tekstPoruke: string;
  korisnickoIme: string;

  constructor(
    naslovPoruke: string,
    tekstPoruke: string,
    korisnickoIme: string,
  ) {
    this.naslovPoruke = naslovPoruke;
    this.tekstPoruke = tekstPoruke;
    this.korisnickoIme = korisnickoIme;
  }

}
