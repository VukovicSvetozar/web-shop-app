export class KomentarOdgovor {

    id: number;
    komentar: string;
    vrijeme: string;
    avatarUrl: string;
    korisnickoIme: string;
    proizvodId: number;


    constructor(
        id: number,
        komentar: string,
        vrijeme: string,
        avatarUrl: string,
        korisnickoIme: string,
        proizvodId: number) {
        this.id = id;
        this.komentar = komentar;
        this.vrijeme = vrijeme;
        this.avatarUrl = avatarUrl;
        this.korisnickoIme = korisnickoIme;
        this.proizvodId = proizvodId;
    }

}
