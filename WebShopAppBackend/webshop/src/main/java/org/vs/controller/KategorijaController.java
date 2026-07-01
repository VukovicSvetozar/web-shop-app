package org.vs.controller;

import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.vs.dto.KategorijaOdgovor;
import org.vs.dto.SpecifikacijaOdgovor;
import org.vs.service.KategorijaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/kategorije")
@RequiredArgsConstructor
public class KategorijaController {

	private final KategorijaService kategorijaService;

	@GetMapping("")
	public ResponseEntity<Set<KategorijaOdgovor>> vratiSveKategorije() {
		Set<KategorijaOdgovor> kategorije = kategorijaService.vratiSveKategorije();
		if (kategorije.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else
			return new ResponseEntity<>(kategorije, HttpStatus.OK);
	}

	@GetMapping("/{kategorijaId}/specifikacije")
	public ResponseEntity<Set<SpecifikacijaOdgovor>> vratiSveSpecifikacijePoIdKategorije(
			@PathVariable(value = "kategorijaId") @Valid Integer kategorijaId) {
		Set<SpecifikacijaOdgovor> specifikacije = kategorijaService.vratiSveSpecifikacijePoIdKategorije(kategorijaId);
		return new ResponseEntity<>(specifikacije, HttpStatus.OK);
	}

}
