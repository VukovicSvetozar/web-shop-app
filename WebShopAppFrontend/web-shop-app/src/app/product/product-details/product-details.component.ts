import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { ProizvodDetaljiOdgovor } from 'src/app/model/proizvodDetaljiOdgovor';
import { BackendService } from 'src/app/services/backend.service';
import { PurchaseModalComponent } from '../purchase-modal/purchase-modal.component';
import { CommentModalComponent } from '../comment-modal/comment-modal.component';
import { PrijavaOdgovor } from 'src/app/model/prijavaOdgovor';
import { LoginService } from 'src/app/services/login.service';
import { KomentarInfo } from 'src/app/model/komentarInfo';
import { KupovinaInfo } from 'src/app/model/kupovinaInfo';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.css']
})
export class ProductDetailsComponent implements OnInit, OnDestroy {

  proizvodId: string = "";
  odabraniProizvod: ProizvodDetaljiOdgovor | null = null;
  avatarUrl: string = "";
  specificneOsobine: { [key: string]: string } = {};
  slikeProizvodaUrl: string[] = [];
  slikeTrenutniIndeks: number = 0;
  slikeTimeoutId?: number;
  korisnickoIme: string;
  prijavljen: boolean;
  dozvoljenKomentar: boolean = false;
  dozvoljenaKupovina: boolean = false;
  dozvoljenoBrisanje: boolean = false;

  constructor(private route: ActivatedRoute, private backendService: BackendService, private snackBar: MatSnackBar,
    private dijalog: MatDialog, private loginService: LoginService, private router: Router) {
    const podaci: PrijavaOdgovor | null = this.loginService.vratiPodatke();
    if (podaci) {
      this.korisnickoIme = podaci.korisnickoIme;
      this.prijavljen = true;
    }
    else {
      this.korisnickoIme = 'gost';
      this.prijavljen = false;
    }
  }

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.proizvodId = params['id'];
    });
    this.dozvoljenKomentar = this.route.snapshot.paramMap.get('dozvoljenKomentar') === 'true';
    this.dozvoljenaKupovina = this.route.snapshot.paramMap.get('dozvoljenaKupovina') === 'true';
    this.dozvoljenoBrisanje = this.route.snapshot.paramMap.get('dozvoljenoBrisanje') === 'true';
    this.ucitajProizvod();
    this.resetTimer();
  }

  ngOnDestroy() {
    window.clearTimeout(this.slikeTimeoutId);
  }

  ucitajProizvod(): void {
    const proizvodIdNumber = parseInt(this.proizvodId, 10);
    this.backendService.vratiProizvod(proizvodIdNumber).subscribe({
      next: (odgovor) => {
        if (odgovor) {
          this.odabraniProizvod = odgovor;
          this.avatarUrl = this.odabraniProizvod.avatarUrl;
          this.specificneOsobine = this.odabraniProizvod.specificneOsobine;
          this.slikeProizvodaUrl = this.odabraniProizvod.slikeProizvodaUrl;
        }
      },
      error: () => {
        this.snackBar.open('Došlo je do greške prilikom ucitavanja detalja o proizvodu!', 'Zatvori', { duration: 5000 });
      }
    })
  }

  resetTimer() {
    if (this.slikeTimeoutId) {
      window.clearTimeout(this.slikeTimeoutId);
    }
    this.slikeTimeoutId = window.setTimeout(() => this.sledecaSlika(), 4000);
  }

  prethodnaSlika(): void {
    const prviSlajd = this.slikeTrenutniIndeks === 0;
    const noviIndeks = prviSlajd
      ? this.slikeProizvodaUrl.length - 1
      : this.slikeTrenutniIndeks - 1;
    this.resetTimer();
    this.slikeTrenutniIndeks = noviIndeks;
  }

  sledecaSlika(): void {
    const poslednjiSlajd = this.slikeTrenutniIndeks === this.slikeProizvodaUrl.length - 1;
    const noviIndeks = poslednjiSlajd ? 0 : this.slikeTrenutniIndeks + 1;
    this.resetTimer();
    this.slikeTrenutniIndeks = noviIndeks;
  }

  idiNaSlajd(slajdIndeks: number): void {
    this.resetTimer();
    this.slikeTrenutniIndeks = slajdIndeks;
  }

  vratiTrenutniSlajdUrl() {
    return `url('${this.slikeProizvodaUrl[this.slikeTrenutniIndeks]}')`;
  }

  otvoriKupovinaDijalog(): void {
    var kupovinaInfo: KupovinaInfo = new KupovinaInfo(this.korisnickoIme, Number(this.proizvodId));
    const dijalogRef = this.dijalog.open(PurchaseModalComponent, {
      width: '400px',
      data: kupovinaInfo
    });
    dijalogRef.afterClosed().subscribe((result: string) => {
      if (result) {
        console.log("Main component", result);
      }
    });
  }

  otvoriKomentariDijalog(): void {
    var komentarInfo: KomentarInfo = new KomentarInfo(this.korisnickoIme, Number(this.proizvodId));
    const dijalogRef = this.dijalog.open(CommentModalComponent, {
      width: '700px',
      data: komentarInfo
    });
    dijalogRef.afterClosed().subscribe((result: string) => {
      if (result) {
        console.log("Main component", result);
      }
    });
  }

  obrisiProizvod(): void {
    if (confirm("Da li ste sigurni?")) {
      this.backendService.obrisiProizvod(this.korisnickoIme, Number(this.proizvodId)).subscribe({
        next: () => {
          this.snackBar.open('Uspješno ste obrisali proizvod!', 'Zatvori', { duration: 4000 });
          this.router.navigate(['/../../offer']);
        },
        error: () => {
          this.snackBar.open('Doslo je do greske prilikom brisanja proizvoda! ', 'Zatvori', { duration: 4000 });
        }
      })
    }
  }

}
