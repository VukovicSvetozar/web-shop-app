import { HttpClient, HttpErrorResponse, HttpEvent, HttpParams, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { AktivacijaZahtjev } from 'src/app/model/aktivacijaZahtjev';
import { KorisnikInfo } from 'src/app/model/korisnikInfo';
import { PrijavaOdgovor } from 'src/app/model/prijavaOdgovor';
import { PrijavaZahtjev } from 'src/app/model/prijavaZahtjev';
import { RegistracijaZahtjev } from 'src/app/model/registracijaZahtjev';
import { TestZahtjev } from 'src/app/model/testZahtjev';
import { environment } from 'src/environments/environment';
import { PorukaPodrskeZahtjev } from '../model/porukaPodrskeZahtjev';
import { KategorijaOdgovor } from '../model/kategorijaOdgovor';
import { SpecifikacijaOdgovor } from '../model/specifikacijaOdgovor';
import { ProizvodZahtjev } from '../model/proizvodZahtjev';
import { ProizvodSpecifikacijaZahtjev } from '../model/proizvodSpecifikacijaZahtjev';
import { SlikaInfo } from '../model/slikaInfo';
import { ProizvodDetaljiOdgovor } from '../model/proizvodDetaljiOdgovor';
import { PretragaZahtjev } from '../model/pretragaZahtjev';
import { KomentarZahtjev } from '../model/komentarZahtjev';
import { KupovinaZahtjev } from '../model/kupovinaZahtjev';
import { StatistikaOdgovor } from '../model/statistikaOdgovor';

@Injectable({
  providedIn: 'root'
})
export class BackendService {

  constructor(private http: HttpClient) {
  }

  // Autentikacija i autorizacija

  public provjeriDostupnostKorisnickogImena(korisnickoIme: string): Observable<boolean> {
    const url = environment.API_URL + `autentifikacija/dostupnost/korisnicko-ime/${korisnickoIme}`;
    return this.http.get<boolean>(url);
  }

  public provjeriDostupnostEmaila(email: string): Observable<boolean> {
    const url = environment.API_URL + `autentifikacija/dostupnost/email/${email}`;
    return this.http.get<boolean>(url);
  }

  public registracija(zahtjev: RegistracijaZahtjev) {
    return this.http.post(environment.API_URL + 'autentifikacija/registracija', zahtjev).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error)
      })
    );
  }

  public aktivacija(zahtjev: AktivacijaZahtjev): Observable<PrijavaOdgovor> {
    return this.http.post<PrijavaOdgovor>(environment.API_URL + 'autentifikacija/aktivacija', zahtjev).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error)
      })
    );
  }

  public prijava(zahtjev: PrijavaZahtjev): Observable<PrijavaOdgovor> {
    return this.http.post<PrijavaOdgovor>(environment.API_URL + 'autentifikacija/prijava', zahtjev).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error)
      })
    );
  }

  public vratiPodatkeKorisnika(korisnickoIme: string): Observable<KorisnikInfo> {
    const url = environment.API_URL + `korisnik/${korisnickoIme}`;
    return this.http.get<KorisnikInfo>(url);
  }

  public azuriraj(zahtjev: KorisnikInfo) {
    return this.http.post(environment.API_URL + 'korisnik/azuriraj', zahtjev).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error)
      })
    );
  }

  public posaljiPorukuPodrsci(zahtjev: PorukaPodrskeZahtjev) {
    return this.http.post(environment.API_URL + 'porukePodrske', zahtjev).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error)
      })
    );
  }

  public vratiSveKategorije(): Observable<Set<KategorijaOdgovor>> {
    const url = environment.API_URL + `kategorije`;
    return this.http.get<Set<KategorijaOdgovor>>(url);
  }

  public vratiSpecifikacijePoIdKategorije(kategorijaId: number): Observable<Set<SpecifikacijaOdgovor>> {
    const url = environment.API_URL + `kategorije/${kategorijaId}/specifikacije`;
    return this.http.get<Set<SpecifikacijaOdgovor>>(url);
  }

  public dodajProizvod(zahtjev: ProizvodZahtjev): Observable<number> {
    return this.http.post<number>(environment.API_URL + 'proizvodi', zahtjev).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error)
      })
    );
  }

  public odustaniOdKreiranjaProizvoda(proizvodId: number) {
    const url = environment.API_URL + `proizvodi/${proizvodId}`;
    return this.http.delete(url);
  }

  public obrisiProizvod(korisnickoIme: string, proizvodId: number) {
    const url = environment.API_URL + `proizvodi/${korisnickoIme}/${proizvodId}`;
    return this.http.delete(url);
  }

  public dodajProizvodSpecifikaciju(zahtjev: ProizvodSpecifikacijaZahtjev) {
    return this.http.post(environment.API_URL + 'proizvodi/specifikacije', zahtjev).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error)
      })
    );
  }

  public dodajSlike(file: File, proizvodId: number, isPoster: boolean): Observable<HttpEvent<any>> {
    const formData: FormData = new FormData();
    const posterPrefiks = isPoster ? '1_' : '0_';
    const modifikovanoImeSlike = proizvodId + "_" + posterPrefiks + file.name;
    const modifikovanFile = new File([file], modifikovanoImeSlike, { type: file.type });
    formData.append('file', modifikovanFile);
    const req = new HttpRequest('POST', environment.API_URL + 'proizvodi/slika', formData, {
      reportProgress: true
    });
    return this.http.request(req);
  }

  // Proizvodi

  public vratiSveKarticeProizvoda(trenutnaStranica: number, brojElemenata: number, sortiranje: string) {
    const params = new HttpParams()
      .set('trenutnaStranica', trenutnaStranica.toString())
      .set('brojElemenata', brojElemenata.toString())
      .set('sortiranje', sortiranje);
    return this.http.get<any>(environment.API_URL + 'proizvodi/javni/kartice', { params });
  }

  public vratiProizvod(proizvodId: number): Observable<ProizvodDetaljiOdgovor> {
    const url = environment.API_URL + `proizvodi/javni/${proizvodId}`;
    return this.http.get<ProizvodDetaljiOdgovor>(url);
  }

  public vratiPoster(proizvodId: number): Observable<SlikaInfo> {
    const url = environment.API_URL + `proizvodi/javni/slika/${proizvodId}/poster`;
    return this.http.get<SlikaInfo>(url);
  }

  public vratiAktivneKarticeProizvodaKorisnika(korisnickoIme: string, trenutnaStranica: number, brojElemenata: number, sortiranje: string) {
    const params = new HttpParams()
      .set('trenutnaStranica', trenutnaStranica.toString())
      .set('brojElemenata', brojElemenata.toString())
      .set('sortiranje', sortiranje);
    return this.http.get<any>(environment.API_URL + `proizvodi/kartice/aktivno/korisnik/${korisnickoIme}`, { params });
  }

  public vratiZavrseneKarticeProizvodaKorisnika(korisnickoIme: string, trenutnaStranica: number, brojElemenata: number) {
    const params = new HttpParams()
      .set('trenutnaStranica', trenutnaStranica.toString())
      .set('brojElemenata', brojElemenata.toString());
    return this.http.get<any>(environment.API_URL + `proizvodi/kartice/zavrseno/korisnik/${korisnickoIme}`, { params });
  }

  public vratiKarticeKupljenihProizvodaKorisnika(korisnickoIme: string, trenutnaStranica: number, brojElemenata: number) {
    const params = new HttpParams()
      .set('trenutnaStranica', trenutnaStranica.toString())
      .set('brojElemenata', brojElemenata.toString());
    return this.http.get<any>(environment.API_URL + `proizvodi/kartice/kupljeno/${korisnickoIme}`, { params });
  }

  public vratiStatistiku(korisnickoIme: string): Observable<StatistikaOdgovor> {
    const url = environment.API_URL + `proizvodi/statistika/${korisnickoIme}`;
    return this.http.get<StatistikaOdgovor>(url);
  }

  // Pretraga

  public pretragaProizvoda(zahtjev: PretragaZahtjev, trenutnaStranica: number, brojElemenata: number) {
    const params = new HttpParams()
      .set('trenutnaStranica', trenutnaStranica.toString())
      .set('brojElemenata', brojElemenata.toString());
    return this.http.post<any>(environment.API_URL + 'proizvodi/javni/kartice/pretraga', zahtjev, { params });
  }

  // Komentari

  public vratiSveKomentare(proizvodId: number, trenutnaStranica: number, brojElemenata: number) {
    const params = new HttpParams()
      .set('trenutnaStranica', trenutnaStranica.toString())
      .set('brojElemenata', brojElemenata.toString());
    return this.http.get<any>(environment.API_URL + `proizvodi/komentar/${proizvodId}`, { params });
  }

  public dodajKomentar(zahtjev: KomentarZahtjev) {
    return this.http.post(environment.API_URL + 'proizvodi/komentar', zahtjev).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error)
      })
    );
  }

  // Kupovina

  public kupi(zahtjev: KupovinaZahtjev) {
    return this.http.post(environment.API_URL + 'kupovina', zahtjev).pipe(
      catchError((error: HttpErrorResponse) => {
        return throwError(() => error)
      })
    );
  }

}
