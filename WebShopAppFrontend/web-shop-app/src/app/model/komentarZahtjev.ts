export class KomentarZahtjev {

    komentar: string;
    korisnickoIme: string;
    proizvodId: number;

    constructor(
        komentar: string,
        korisnickoIme: string,
        proizvodId: number) {
        this.komentar = komentar;
        this.korisnickoIme = korisnickoIme;
        this.proizvodId = proizvodId;
    }

}
