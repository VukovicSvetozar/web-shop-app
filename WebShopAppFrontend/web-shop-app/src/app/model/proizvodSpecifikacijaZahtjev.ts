export class ProizvodSpecifikacijaZahtjev {

  vrijednost: string;
  proizvodId: number;
  specifikacijaId: number;

  constructor(
    vrijednost: string,
    proizvodId: number,
    specifikacijaId: number
  ) {
    this.vrijednost = vrijednost;
    this.proizvodId = proizvodId;
    this.specifikacijaId = specifikacijaId;
  }

}
