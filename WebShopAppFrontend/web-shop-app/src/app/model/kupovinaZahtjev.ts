export class KupovinaZahtjev {

    nazivKurirskeSluzbe: string;
    brojKartice: string;
    proizvodId: number;
    korisnickoImeKupca: string;

    constructor(
        nazivKurirskeSluzbe: string,
        brojKartice: string,
        proizvodId: number, 
        korisnickoImeKupca: string) {
        this.nazivKurirskeSluzbe = nazivKurirskeSluzbe;
        this.brojKartice = brojKartice;
        this.proizvodId = proizvodId;
        this.korisnickoImeKupca = korisnickoImeKupca;
    }

}
