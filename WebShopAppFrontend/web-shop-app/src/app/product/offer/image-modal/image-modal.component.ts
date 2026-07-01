import { HttpEventType, HttpResponse } from '@angular/common/http';
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Observable } from 'rxjs';
import { BackendService } from 'src/app/services/backend.service';

@Component({
  selector: 'app-image-modal',
  templateUrl: './image-modal.component.html',
  styleUrls: ['./image-modal.component.css']
})
export class ImageModalComponent {

  constructor(private dialogRef: MatDialogRef<ImageModalComponent>, private backendService: BackendService,
    private snackBar: MatSnackBar, @Inject(MAT_DIALOG_DATA) public proizvodId: number | null) { }

  ngOnInit(): void {
    // this.imageInfos = this.backendService.vratiSlike();
    // this.imageInfos = this.backendService.vratiSlikeProizvoda(this.proizvodId!);
    this.imageInfos = this.backendService.vratiPoster(this.proizvodId!);
  }

  odabraneSlike?: FileList;
  preglediSlika: string[] = [];
  selektovaniPoster: string | null = null;
  omoguciKraj: boolean = false;

  imageInfos?: Observable<any>;       // koristi se da prihvati slike sa servera

  odaberiSlike(event: any): void {
    this.selektovaniPoster = null;
    this.odabraneSlike = event.target.files;
    if (this.odabraneSlike && this.odabraneSlike[0]) {
      const brojSlika = this.odabraneSlike.length;
      for (let i = 0; i < brojSlika; i++) {
        const reader = new FileReader();
        reader.onload = (e: any) => {
          this.preglediSlika.push(e.target.result);
        };
        reader.readAsDataURL(this.odabraneSlike[i]);
      }
    }
  }

  dodajSlike(): void {
    if (this.odabraneSlike) {
      for (let i = 0; i < this.odabraneSlike.length; i++) {
        const isPoster = this.selektovaniPoster === this.preglediSlika[i];
        this.ucitaj(this.odabraneSlike[i], isPoster);
      }
      this.selektovaniPoster = null;
      this.omoguciKraj = true;
    }
  }

  ucitaj(file: File, isPoster: boolean): void {
    if (file) {
      this.backendService.dodajSlike(file, this.proizvodId!, isPoster).subscribe({
        next: () => {
          const obavjestenje = file.name + " je dodato!";
          this.snackBar.open(obavjestenje, 'Zatvori', { duration: 10000 });
        },
        error: () => {
          this.snackBar.open('Doslo je do greske tokom dodavanja slike!', 'Zatvori', { duration: 10000 });
        }
      });
    }
  }

  selektujPoster(pregled: string): void {
    this.selektovaniPoster = pregled;
  }

  zavrsi() {
    this.dialogRef.close();
  }

}
