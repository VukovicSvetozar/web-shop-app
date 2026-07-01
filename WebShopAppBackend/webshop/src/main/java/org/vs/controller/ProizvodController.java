package org.vs.controller;

import java.util.*;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.vs.dto.*;
import org.vs.service.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/proizvodi")
@RequiredArgsConstructor
public class ProizvodController {

	private final ProizvodService proizvodService;
	private final SlikaService slikaService;

	@GetMapping("/javni/kartice")
	public ResponseEntity<Map<String, Object>> vratiSveKarticeProizvoda(
			@RequestParam(required = false) Integer kategorijaId,
			@RequestParam(defaultValue = "0") int trenutnaStranica, @RequestParam(defaultValue = "9") int brojElemenata,
			@RequestParam(defaultValue = "id,o") String[] sortiranje) {
		Map<String, Object> response = proizvodService.vratiSveKarticeProizvoda(kategorijaId, trenutnaStranica,
				brojElemenata, sortiranje);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/kartice/korisnik/{korisnickoIme}")
	public ResponseEntity<Map<String, Object>> vratiKarticeProizvodaKorisnika(
			@PathVariable(value = "korisnickoIme") String korisnickoIme,
			@RequestParam(defaultValue = "0") int trenutnaStranica, @RequestParam(defaultValue = "9") int brojElemenata,
			@RequestParam(defaultValue = "id,o") String[] sortiranje) {
		Map<String, Object> response = proizvodService.vratiKarticeProizvodaKorisnika(korisnickoIme, trenutnaStranica,
				brojElemenata, sortiranje);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/kartice/aktivno/korisnik/{korisnickoIme}")
	public ResponseEntity<Map<String, Object>> vratiAktivneKarticeProizvodaKorisnika(
			@PathVariable(value = "korisnickoIme") String korisnickoIme,
			@RequestParam(defaultValue = "0") int trenutnaStranica,
			@RequestParam(defaultValue = "4") int brojElemenata) {
		Map<String, Object> response = proizvodService.vratiAktivneKarticeProizvodaKorisnika(korisnickoIme,
				trenutnaStranica, brojElemenata);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/kartice/zavrseno/korisnik/{korisnickoIme}")
	public ResponseEntity<Map<String, Object>> vratiZavrseneKarticeProizvodaKorisnika(
			@PathVariable(value = "korisnickoIme") String korisnickoIme,
			@RequestParam(defaultValue = "0") int trenutnaStranica,
			@RequestParam(defaultValue = "4") int brojElemenata) {
		Map<String, Object> response = proizvodService.vratiZavrseneKarticeProizvodaKorisnika(korisnickoIme,
				trenutnaStranica, brojElemenata);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/kartice/kupljeno/{korisnickoIme}")
	public ResponseEntity<Map<String, Object>> vratiKarticeKupljenihProizvodaKorisnika(
			@PathVariable(value = "korisnickoIme") String korisnickoIme,
			@RequestParam(defaultValue = "0") int trenutnaStranica, @RequestParam(defaultValue = "9") int brojElemenata,
			@RequestParam(defaultValue = "id,o") String[] sortiranje) {
		Map<String, Object> response = proizvodService.vratiKarticeKupljenihProizvodaKorisnika(korisnickoIme,
				trenutnaStranica, brojElemenata, sortiranje);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/javni/kartice/pretraga")
	public ResponseEntity<Map<String, Object>> pretragaKarticaProizvoda(@RequestBody @Valid PretragaZahtjev zahtjev,
			@RequestParam(defaultValue = "0") int trenutnaStranica, @RequestParam(defaultValue = "9") int brojElemenata,
			@RequestParam(defaultValue = "id,o") String[] sortiranje) {
		Map<String, Object> response = proizvodService.pretragaKarticaProizvoda(zahtjev, trenutnaStranica,
				brojElemenata, sortiranje);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/javni/{proizvodId}")
	public ResponseEntity<ProizvodDetaljiOdgovor> vratiProizvodPoId(@PathVariable(value = "proizvodId") Integer id) {
		ProizvodDetaljiOdgovor proizvod = proizvodService.vratiProizvodPoId(id);
		return new ResponseEntity<>(proizvod, HttpStatus.OK);
	}

	@GetMapping("/statistika/{korisnickoIme}")
	public ResponseEntity<ProizvodStatistikaOdgovor> vratiStatistiku(
			@PathVariable(value = "korisnickoIme") String korisnickoIme) {
		ProizvodStatistikaOdgovor statistika = proizvodService.vratiStatistiku(korisnickoIme);
		return new ResponseEntity<>(statistika, HttpStatus.OK);
	}

	@PostMapping("")
	public ResponseEntity<Integer> dodajProizvod(@RequestBody @Valid ProizvodZahtjev zahtjev) {
		Integer proizvodId = proizvodService.dodajProizvod(zahtjev);
		return new ResponseEntity<>(proizvodId, HttpStatus.CREATED);
	}

	@DeleteMapping("/{proizvodId}")
	public ResponseEntity<Void> odustaniOdKreiranjaProizvoda(
			@PathVariable(value = "proizvodId") @Valid Integer proizvodId) {
		proizvodService.odustaniOdKreiranjaProizvoda(proizvodId);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{korisnickoIme}/{proizvodId}")
	public ResponseEntity<Void> obrisiProizvod(@PathVariable(value = "korisnickoIme") String korisnickoIme,
			@PathVariable(value = "proizvodId") @Valid Integer proizvodId) {
		proizvodService.obrisiProizvod(korisnickoIme, proizvodId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/specifikacije")
	public ResponseEntity<Void> dodajProizvodSpecifikaciju(@RequestBody @Valid ProizvodSpecifikacijaZahtjev zahtjev) {
		proizvodService.dodajProizvodSpecifikaciju(zahtjev);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	@PostMapping("/slika")
	public ResponseEntity<Void> dodajSliku(@RequestParam("file") @Valid MultipartFile file) {
		try {
			slikaService.sacuvaj(file);
			return ResponseEntity.status(HttpStatus.OK).build();
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
		}
	}

	@GetMapping("/javni/slika/{proizvodId}/poster")
	public ResponseEntity<SlikaInfo> vratiPoster(@PathVariable Integer proizvodId) {
		SlikaInfo fileInfos = slikaService.vratiPoster(proizvodId).map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(ProizvodController.class, "vratiSliku", path.getFileName().toString()).build()
					.toString();
			return new SlikaInfo(filename, url);
		}).findFirst().get();
		return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
	}

	@GetMapping("/javni/slika/{filename:.+}")
	public ResponseEntity<Resource> vratiSliku(@PathVariable String filename) {
		Resource file = slikaService.vratiSliku(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@GetMapping("/komentar/{proizvodId}")
	public ResponseEntity<Map<String, Object>> vratiKomentareProizvoda(
			@PathVariable(value = "proizvodId") Integer proizvodId,
			@RequestParam(defaultValue = "0") int trenutnaStranica,
			@RequestParam(defaultValue = "4") int brojElemenata) {
		Map<String, Object> response = proizvodService.vratiKomentareProizvoda(proizvodId, trenutnaStranica,
				brojElemenata);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/komentar")
	public ResponseEntity<Void> dodajKomentar(@RequestBody @Valid ProizvodKomentarZahtjev zahtjev) {
		proizvodService.dodajKomentar(zahtjev);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/komentar/{komentarId}")
	public ResponseEntity<Boolean> obrisiKomentarProizvoda(@PathVariable(value = "komentarId") Integer komentarId) {
		boolean obrisan = proizvodService.obrisiKomentarProizvoda(komentarId);
		return ResponseEntity.ok(obrisan);
	}

}
