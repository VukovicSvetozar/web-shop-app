<br>
<br>
<div align="center">
  <h1>Web Shop App</h1> 
</div>
<br>
<br>
<br>

<div style="page-break-before: always;"></div>

**Web Shop App (ETFBL_IP)** je sistem sačinjen od četiri samostalne aplikacije koje simuliraju online oglasnik: web prodavnica za krajnje korisnike (Angular + Spring Boot), administratorska aplikacija za upravljanje kategorijama, korisnicima i statistikom (JSP, Model 2), i aplikacija za korisničku podršku (JSP). Projekat je realizovan za predmet *Internet programiranje*.

Sve tri prijave (web prodavnica, administratorska aplikacija, korisnička podrška) su potpuno nezavisne jedna od druge, iako dijele istu bazu podataka.

---

## 🛠️ Ključne Funkcionalnosti

### 1. Web prodavnica – oglasi, pretraga i kupovina
* **Oglasi po kategorijama:** Svaki oglas ima naslov, opis, cijenu, stanje (novo/polovno), lokaciju, slike i kontakt prodavca, a svaka kategorija (vozila, nekretnine, računari...) nosi i sopstveni skup specifičnih atributa definisanih u administratorskoj aplikaciji.
* **Pretraga, filtriranje i straničenje:** Oglasi se prikazuju kao kartice (naziv, slika, cijena) uz straničenje, filtriranje i pretragu po kategoriji i njenim specifičnim atributima; klikom na karticu otvara se stranica sa svim detaljima.
* **Komentari na oglas:** Svaki oglas ima konverzaciju u vidu pitanja/komentara koju vide svi korisnici, dok neregistrovani posjetioci mogu samo pregledati ponude, bez postavljanja pitanja ili kupovine.
* **Kupovina i istorija:** Kupovina se evidentira uz odabir načina plaćanja (kartica, pouzeće), a svaki korisnik ima pregled sopstvenih kupovina, kao i pregled i brisanje svojih aktivnih i završenih oglasa.

### 2. Registracija, aktivacija naloga i sigurnost
* **Registracija sa PIN aktivacijom:** Nakon registracije (ime, prezime, grad, korisničko ime, lozinka, avatar, mail) korisniku se šalje četvorocifreni PIN na mail; nalog postaje aktivan tek nakon unosa PIN-a, a ponovni pokušaj prijave na neaktivan nalog ponovo generiše i šalje PIN.
* **JWT autentifikacija:** REST API je zaštićen putem JSON Web Tokena (Spring Security + `jjwt`), a prijavljeni korisnik može uređivati sve svoje podatke osim korisničkog imena.
* **Kontakt podrške:** Prijavljeni korisnici mogu poslati poruku korisničkoj podršci direktno iz aplikacije.

### 3. Administratorska aplikacija (JSP, Model 2)
* **Zaseban administratorski nalog:** Prijava koristi nalog kreiran direktno u bazi, nezavisan od naloga web prodavnice.
* **Upravljanje kategorijama:** CRUD nad kategorijama i njihovim specifičnim atributima (npr. za nekretnine: tip, sprat, kvadratura).
* **Upravljanje korisnicima:** CRUD nad korisničkim nalozima web prodavnice.
* **Statistika:** Pregled logova Spring Boot backend aplikacije.

### 4. Aplikacija za korisničku podršku (JSP)
* **Zaseban operaterski nalog:** Prijava potpuno odvojena od naloga web prodavnice i administratorske aplikacije.
* **Pregled poruka:** Operater vidi sve pristigle poruke, s tim da se otvaranjem poruke njen status mijenja u pročitanu; poruke se mogu pretraživati po sadržaju.
* **Odgovor mail-om:** Na poruku korisnika operater odgovara slanjem e-mail poruke.

---

## 🏗️ Arhitektura

* **Četiri nezavisne aplikacije, jedna baza:** Web prodavnica (Angular + Spring Boot REST API), administratorska aplikacija (JSP) i aplikacija za korisničku podršku (JSP) dijele istu MySQL bazu `webshopapp`, ali svaka ima sopstveni, potpuno odvojen sistem naloga.
* **DTO sloj (Zahtjev/Odgovor):** Spring Boot API komunicira isključivo preko posebnih zahtjev/odgovor DTO klasa (npr. `ProizvodKarticaOdgovor` za listu oglasa, `ProizvodDetaljiOdgovor` za stranicu detalja), uz `ModelMapper` za konverziju iz JPA entiteta.
* **Dinamični atributi po kategoriji:** Specifični atributi svake kategorije modelovani su kroz `Specifikacija` i `ProizvodSpecifikacija` (sa složenim ključem), umjesto fiksnih kolona po kategoriji proizvoda.
* **Bez logike u bazi:** U skladu sa zahtjevom zadatka, baza ne sadrži uskladištene procedure ni okidače – sva poslovna logika i validacija nalaze se u aplikativnom sloju (Spring Boot servisi i klijentska/serverska validacija).
* **JSP Model 2 vs. jednostavniji JSP:** Administratorska aplikacija je rađena po Model 2 obrascu (servleti kao kontroleri, JSP samo za prikaz), dok je aplikacija za korisničku podršku jednostavnija JSP aplikacija – razlika koju nalaže sama specifikacija zadatka.

---

## 📁 Struktura Projekta

```text
WebShopAppBackend/webshop/            # Spring Boot REST API
└── src/main/java/org/vs/
    ├── controller/                    # REST kontroleri
    ├── service/                        # Poslovna logika (mail, upload slika...)
    ├── repository/                      # Spring Data JPA repozitorijumi
    ├── entity/                           # JPA entiteti
    ├── dto/                               # Zahtjev/Odgovor DTO klase
    ├── security/                           # JWT autentifikacija, Spring Security konfiguracija
    └── exception/                           # Obrada grešaka

WebShopAppFrontend/web-shop-app/      # Angular SPA
└── src/app/
    ├── auth/                          # Prijava, registracija, aktivacija naloga
    ├── product/                        # Pregled, pretraga i kreiranje oglasa
    ├── support/                          # Forma za kontaktiranje podrške
    ├── app-layout/                        # Zajednički izgled stranica
    └── services/                           # HTTP komunikacija sa backendom

AdministratorskaAplikacija/           # JSP (Model 2) administratorski panel
└── src/main/java/
    ├── controller/                    # Servleti (kategorije, korisnici, statistika)
    ├── dao/                            # Pristup bazi
    └── beans/                          # Modeli podataka

KorisnickaPodrska/                    # JSP aplikacija za korisničku podršku
└── src/main/java/
    ├── service/                       # Prijava operatera, pristup bazi
    └── beans/                         # Modeli podataka

Tekst projekta/Zadatak.pdf            # Specifikacija zadatka
```

---

## 💻 Tehnologije i Alati

* **Backend:** Spring Boot (Java), Spring Data JPA/Hibernate, Spring Security + JWT (`jjwt`), ModelMapper, Spring Mail, MySQL
* **Frontend:** Angular 16, Angular Material + CDK
* **Administratorska aplikacija i podrška:** JSP, Servleti (Model 2 za administratorsku aplikaciju), sopstveni connection pool
* **Baza podataka:** MySQL – bez uskladištenih procedura ili okidača (ograničenje zadatka); sva logika je u aplikativnom sloju
* **Komunikacija:** RESTful API između Angular frontenda i Spring Boot backenda

---

## 🚀 Kako Pokrenuti Projekat Lokalno

Sve tri Java komponente dijele istu MySQL bazu (`webshopapp`); Spring Boot je podešen da šemu kreira/ažurira automatski pri prvom pokretanju (`spring.jpa.hibernate.ddl-auto=update`).

### Backend (`WebShopAppBackend/webshop`)
1. U `src/main/resources/application.properties` podesiti pristup bazi, nalog za slanje mail-a (PIN aktivacija) i JWT tajni ključ.
2. Pokrenuti: `./mvnw spring-boot:run` (podrazumijevano na portu 8888).

### Frontend (`WebShopAppFrontend/web-shop-app`)
1. `npm install`
2. `ng serve` (ili `npm start`)

### Administratorska aplikacija / Korisnička podrška
1. Podesiti `ConnectionPool.properties` u odgovarajućem `oodb` paketu svake aplikacije.
2. Build-ovati kao WAR i postaviti na servletski kontejner (npr. Apache Tomcat).
