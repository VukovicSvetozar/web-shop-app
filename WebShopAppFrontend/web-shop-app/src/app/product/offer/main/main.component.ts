import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { CategoryModalComponent } from '../category-modal/category-modal.component';
import { MatPaginatorIntl, PageEvent } from '@angular/material/paginator';
import { ProizvodKarticaOdgovor } from 'src/app/model/proizvodKarticaOdgovor';
import { BackendService } from 'src/app/services/backend.service';
import { LoginService } from 'src/app/services/login.service';
import { PrijavaOdgovor } from 'src/app/model/prijavaOdgovor';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  listaAktivnihKarticaProizvoda: ProizvodKarticaOdgovor[] = [];
  listaZavrsenihKarticaProizvoda: ProizvodKarticaOdgovor[] = [];
  listaKupljenihKarticaProizvoda: ProizvodKarticaOdgovor[] = [];
  trenutnaStranicaAktivno: number = 0;
  ukupnoElemenataAktivno: number = 0;
  brojStranicaAktivno: number = 0;
  brojElemenataAktivno: number = 4;
  trenutnaStranicaZavrseno: number = 0;
  ukupnoElemenataZavrseno: number = 0;
  brojStranicaZavrseno: number = 0;
  brojElemenataZavrseno: number = 4;
  trenutnaStranicaKupljeno: number = 0;
  ukupnoElemenataKupljeno: number = 0;
  brojStranicaKupljeno: number = 0;
  brojElemenataKupljeno: number = 4;
  korisnickoIme: string;
  statistikaUkupnoElemenataAktivno: number = 0;
  statistikaUkupnoElemenataZavrseno: number = 0;
  statistikaUkupnoElemenataKupljeno: number = 0;

  constructor(private dijalog: MatDialog, private backendService: BackendService,
    private paginatorIntl: MatPaginatorIntl, private loginService: LoginService) {
    const podaci: PrijavaOdgovor | null = this.loginService.vratiPodatke();
    if (podaci) {
      this.korisnickoIme = podaci.korisnickoIme;
    }
    else {
      this.korisnickoIme = 'gost';
    }
  }

  ngOnInit(): void {
    this.ucitajAktivneKarticeProizvodaKorisnika(this.korisnickoIme, this.trenutnaStranicaAktivno, this.brojElemenataAktivno);
    this.ucitajZavrseneKarticeProizvodaKorisnika(this.korisnickoIme, this.trenutnaStranicaZavrseno, this.brojElemenataZavrseno);
    this.ucitajKarticeKupljenihProizvodaKorisnika(this.korisnickoIme, this.trenutnaStranicaKupljeno, this.brojElemenataKupljeno);
    this.vratiStatistiku(this.korisnickoIme);
    this.paginatorIntl.itemsPerPageLabel = 'Broj elemenata po stranici:';
    this.paginatorIntl.nextPageLabel = 'Sledeća stranica';
    this.paginatorIntl.previousPageLabel = 'Prethodna stranica';
  }

  otvoriDodajPonuduDijalog(): void {
    const dijalogRef = this.dijalog.open(CategoryModalComponent, {
      width: '400px'
    });
    dijalogRef.afterClosed().subscribe((result: string) => {
      if (result) {
        console.log("Main component", result);
      }
    });
  }

  ucitajAktivneKarticeProizvodaKorisnika(korisnickoIme: string, trenutnaStranica: number, brojElemenata: number) {
    const sortiranje = 'id,r'
    this.backendService.vratiAktivneKarticeProizvodaKorisnika(korisnickoIme, trenutnaStranica, brojElemenata, sortiranje).subscribe({
      next: (odgovor) => {
        this.listaAktivnihKarticaProizvoda = odgovor.aktivneKartice;
        this.trenutnaStranicaAktivno = odgovor.trenutnaStranica;
        this.ukupnoElemenataAktivno = odgovor.ukupnoElemenata;
        this.brojStranicaAktivno = odgovor.brojStranica;
      }
    });
  }

  ucitajZavrseneKarticeProizvodaKorisnika(korisnickoIme: string, trenutnaStranica: number, brojElemenata: number) {
    this.backendService.vratiZavrseneKarticeProizvodaKorisnika(korisnickoIme, trenutnaStranica, brojElemenata).subscribe({
      next: (odgovor) => {
        this.listaZavrsenihKarticaProizvoda = odgovor.zavrseneKartice;
        this.trenutnaStranicaZavrseno = odgovor.trenutnaStranica;
        this.ukupnoElemenataZavrseno = odgovor.ukupnoElemenata;
        this.brojStranicaZavrseno = odgovor.brojStranica;
      }
    });
  }

  ucitajKarticeKupljenihProizvodaKorisnika(korisnickoIme: string, trenutnaStranica: number, brojElemenata: number) {
    this.backendService.vratiKarticeKupljenihProizvodaKorisnika(korisnickoIme, trenutnaStranica, brojElemenata).subscribe({
      next: (odgovor) => {
        this.listaKupljenihKarticaProizvoda = odgovor.kupljeneKartice;
        this.trenutnaStranicaKupljeno = odgovor.trenutnaStranica;
        this.ukupnoElemenataKupljeno = odgovor.ukupnoElemenata;
        this.brojStranicaKupljeno = odgovor.brojStranica;
      }
    });
  }

  promjenaStraniceAktivniProizvodiKorisnika(event: PageEvent) {
    const novaStranica = event.pageIndex;
    const velicinaStranice = event.pageSize;
    this.ucitajAktivneKarticeProizvodaKorisnika(this.korisnickoIme, novaStranica, velicinaStranice);
  }

  promjenaStraniceZavrseniProizvodiKorisnika(event: PageEvent) {
    const novaStranica = event.pageIndex;
    const velicinaStranice = event.pageSize;
    this.ucitajZavrseneKarticeProizvodaKorisnika(this.korisnickoIme, novaStranica, velicinaStranice);
  }

  promjenaStraniceKupljeniProizvodi(event: PageEvent) {
    const novaStranica = event.pageIndex;
    const velicinaStranice = event.pageSize;
    this.ucitajKarticeKupljenihProizvodaKorisnika(this.korisnickoIme, novaStranica, velicinaStranice);
  }

  vratiStatistiku(korisnickoIme: string) {
    this.backendService.vratiStatistiku(korisnickoIme).subscribe({
      next: (odgovor) => {
        this.statistikaUkupnoElemenataAktivno = odgovor.brojAktivnih;
        this.statistikaUkupnoElemenataZavrseno = odgovor.brojZavrsenih;
        this.statistikaUkupnoElemenataKupljeno = odgovor.brojKupljenih;
      }
    });
  }

}