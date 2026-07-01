import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { PorukaPodrskeZahtjev } from 'src/app/model/porukaPodrskeZahtjev';
import { BackendService } from 'src/app/services/backend.service';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-support',
  templateUrl: './support.component.html',
  styleUrls: ['./support.component.css']
})
export class SupportComponent implements OnInit {

  podrskaForm!: FormGroup;
  korisnickoIme: string = 'gost';

  constructor(private formBuilder: FormBuilder, private snackBar: MatSnackBar, private router: Router,
    private loginService: LoginService, private backendService: BackendService) {
  }

  ngOnInit(): void {
    this.podrskaForm = this.formBuilder.group({
      naslov: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
      tekst: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(300)]]
    });
    this.korisnickoIme = this.loginService.vratiPodatke().korisnickoIme;
  }

  posaljiPoruku(): void {
    if (this.podrskaForm.invalid) {
      return;
    }

    const zahtjev: PorukaPodrskeZahtjev = {
      naslovPoruke: this.podrskaForm.value.naslov,
      tekstPoruke: this.podrskaForm.value.tekst,
      korisnickoIme: this.korisnickoIme
    }

    this.backendService.posaljiPorukuPodrsci(zahtjev).subscribe({
      next: () => {
        this.snackBar.open('Poruka je uspješno poslata.', 'Početna strana.', { duration: 4000 }).onAction().subscribe(() => {
          this.router.navigate(['/../../home']);
        });
        setTimeout(() => {
          this.router.navigate(['/../../home']);
        }, 4000);
        setTimeout(() => {
          this.router.navigate(['/../../home']);
          this.podrskaForm.reset();
        }, 4000);
      },
      error: (greska) => {
        this.snackBar.open('Došlo je do greške prilikom slanja poruke! ' + greska.error.poruka, 'Zatvori', { duration: 4000 });
      }
    });

  }

}
