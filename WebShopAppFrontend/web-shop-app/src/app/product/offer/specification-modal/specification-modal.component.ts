import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { KategorijaOdgovor } from 'src/app/model/kategorijaOdgovor';
import { ProizvodSpecifikacijaZahtjev } from 'src/app/model/proizvodSpecifikacijaZahtjev';
import { SpecifikacijaInfo } from 'src/app/model/specifikacijaInfo';
import { SpecifikacijaOdgovor } from 'src/app/model/specifikacijaOdgovor';
import { BackendService } from 'src/app/services/backend.service';
import { ImageModalComponent } from '../image-modal/image-modal.component';

@Component({
  selector: 'app-specification-modal',
  templateUrl: './specification-modal.component.html',
  styleUrls: ['./specification-modal.component.css']
})
export class SpecificationModalComponent implements OnInit {

  specifikacijaForm!: FormGroup;
  listaSpecifikacija: SpecifikacijaOdgovor[] = [];

  constructor(private formBuilder: FormBuilder, private dialogRef: MatDialogRef<SpecificationModalComponent>,
    private backendService: BackendService, private dijalog: MatDialog,
    private snackBar: MatSnackBar, @Inject(MAT_DIALOG_DATA) public specifikacijaInfo: SpecifikacijaInfo | null) { }

  ngOnInit(): void {
    this.specifikacijaForm = this.formBuilder.group({});
    this.vratiSpecifikacijePoIdKategorije();
  }

  odustani() {
    this.listaSpecifikacija = [];
    this.backendService.odustaniOdKreiranjaProizvoda(this.specifikacijaInfo!.proizvodId).subscribe({
      next: () => {
        this.snackBar.open('Odustali ste od dodavanja proizvoda!', 'Zatvori', { duration: 4000 });
      },
      error: () => {
        this.snackBar.open('Doslo je do greske!', 'Zatvori', { duration: 4000 });
      }
    })
    this.dialogRef.close();
  }

  nastavi() {
    this.listaSpecifikacija.forEach(specifikacija => {
      const unesenaVrijednost = this.specifikacijaForm.get(specifikacija.naziv)?.value;
      const zahtjev: ProizvodSpecifikacijaZahtjev = {
        vrijednost: unesenaVrijednost,
        proizvodId: this.specifikacijaInfo!.proizvodId,
        specifikacijaId: specifikacija.id
      };
      this.dodajProizvodSpecifikaciju(zahtjev);

    });
    this.otvoriSlikaDijalog();
    this.dialogRef.close();
  }


  vratiSpecifikacijePoIdKategorije(): void {
    this.backendService.vratiSpecifikacijePoIdKategorije(this.specifikacijaInfo!.kategorijaId).subscribe({
      next: (odgovor) => {
        if (odgovor) {
          this.listaSpecifikacija = Array.from(odgovor);
          this.konfigurisiFormu();
        }
      },
      error: () => {
        this.snackBar.open('Došlo je do greške prilikom dobijanja specifikacija!', 'Zatvori', { duration: 4000 });
      }
    })
  }

  konfigurisiFormu(): void {
    this.listaSpecifikacija.forEach(specifikacija => {
      this.specifikacijaForm.addControl(String(specifikacija.naziv), new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(50)]));
    });
  }

  dodajProizvodSpecifikaciju(zahtjev: ProizvodSpecifikacijaZahtjev): void {
    this.backendService.dodajProizvodSpecifikaciju(zahtjev).subscribe({
      next: () => {
      },
      error: () => {
        this.snackBar.open('Došlo je do greške prilikom dodavanja specifikacija proizvoda!', 'Zatvori', { duration: 4000 });
      }
    })
  }

  otvoriSlikaDijalog(): void {
    const dijalogRef = this.dijalog.open(ImageModalComponent, {
      width: '600px',
      data: this.specifikacijaInfo?.proizvodId
    });
  }

}
