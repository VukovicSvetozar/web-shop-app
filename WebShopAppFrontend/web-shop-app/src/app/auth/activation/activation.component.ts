import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BackendService } from 'src/app/services/backend.service';
import { Router } from '@angular/router';
import { AktivacijaZahtjev } from 'src/app/model/aktivacijaZahtjev';
import { LoginService } from 'src/app/services/login.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-activation',
  templateUrl: './activation.component.html',
  styleUrls: ['./activation.component.css']
})
export class ActivationComponent implements OnInit {

  aktivacijaForm!: FormGroup;
  korisnickoIme: string = '';

  constructor(private formBuilder: FormBuilder, private backendService: BackendService,
    private snackBar: MatSnackBar, private router: Router, private loginService: LoginService) {
  }

  ngOnInit(): void {
    this.aktivacijaForm = this.formBuilder.group({
      pin: ['', [Validators.required, Validators.pattern('^[0-9]{4}$')]]
    });
    this.korisnickoIme = this.loginService.vratiKorisnickoIme()!;
    console.log("test",this.korisnickoIme);
  }

  aktivacijaKorisnika(): void {
    if (this.aktivacijaForm.invalid) {
      return;
    }
    const zahtjev: AktivacijaZahtjev = {
      korisnickoIme: this.korisnickoIme,
      pin: this.aktivacijaForm.value.pin
    }

    this.backendService.aktivacija(zahtjev).subscribe({
      next: (odgovor) => {
        this.loginService.sacuvajPodatke(odgovor);
        this.snackBar.open('Nalog je uspješno aktiviran.', 'Početna strana.', { duration: 2000 }).onAction().subscribe(() => {
          this.router.navigate(['/../../home']);
        });
        setTimeout(() => {
          this.router.navigate(['/../../home']);
        }, 4000);
        this.aktivacijaForm.reset();
      },
      error: (greska) => {
        this.snackBar.open('Došlo je do greške prilikom aktivacije naloga! ' + greska.error.poruka, 'Zatvori', { duration: 4000 });
      }
    });

  }

}

