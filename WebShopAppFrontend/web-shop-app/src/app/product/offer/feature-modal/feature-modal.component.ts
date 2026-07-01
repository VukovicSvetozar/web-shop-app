import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BackendService } from 'src/app/services/backend.service';
import { SpecificationModalComponent } from '../specification-modal/specification-modal.component';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProizvodZahtjev } from 'src/app/model/proizvodZahtjev';
import { KategorijaOdgovor } from 'src/app/model/kategorijaOdgovor';
import { LoginService } from 'src/app/services/login.service';
import { SpecifikacijaInfo } from 'src/app/model/specifikacijaInfo';

@Component({
  selector: 'app-feature-modal',
  templateUrl: './feature-modal.component.html',
  styleUrls: ['./feature-modal.component.css']
})
export class FeatureModalComponent {

  svojstvoForm!: FormGroup;
  kategorijaId: number | null = null;
  specifikacijaInfo: SpecifikacijaInfo | null = null;

  constructor(private formBuilder: FormBuilder, private dialogRef: MatDialogRef<FeatureModalComponent>,
    private backendService: BackendService, private loginService: LoginService,
    private snackBar: MatSnackBar, private dijalog: MatDialog, @Inject(MAT_DIALOG_DATA) public odabranaKategorija: KategorijaOdgovor | null) { }

  ngOnInit(): void {
    this.svojstvoForm = this.formBuilder.group({
      kratakNaslov: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(30)]],
      cijena: ['', [Validators.required, Validators.pattern(/^\d+(\.\d{1,2})?$/)]],
      stanje: ['', [Validators.required]],
      lokacija: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(50)]],
      opis: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(300)]]
    });
  }

  odustani() {
    this.kategorijaId = null;
    this.specifikacijaInfo = null;
    this.dialogRef.close();
    this.snackBar.open('Odustali ste od dodavanja proizvoda!', 'Zatvori', { duration: 4000 });
  }

  nastavi(): void {
    if (this.svojstvoForm.invalid) {
      return;
    }
    if (this.odabranaKategorija && this.odabranaKategorija.id !== undefined) {
      this.kategorijaId = this.odabranaKategorija.id;

      const zahtjev: ProizvodZahtjev = {
        kratakNaslov: this.svojstvoForm.value.kratakNaslov,
        detaljanOpis: this.svojstvoForm.value.opis,
        cijena: this.svojstvoForm.value.cijena,
        polovan: this.svojstvoForm.value.stanje === 'polovan',
        lokacija: this.svojstvoForm.value.lokacija,
        kategorijaId: this.kategorijaId,
        korisnickoIme: this.loginService.vratiPodatke().korisnickoIme!
      };

      this.backendService.dodajProizvod(zahtjev).subscribe({
        next: (odgovor) => {
          this.specifikacijaInfo = new SpecifikacijaInfo(this.kategorijaId!, odgovor);
          this.otvoriSpecifikacijeDijalog();
          this.dialogRef.close(this.specifikacijaInfo);
        },
        error: () => {
          this.snackBar.open('Došlo je do greške prilikom dodavanja proizvoda!', 'Zatvori', { duration: 4000 });
        }
      });
    }
  }

  otvoriSpecifikacijeDijalog(): void {
    this.dijalog.open(SpecificationModalComponent, {
      width: '600px',
      data: this.specifikacijaInfo
    });
  }

}
