package org.vs.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vs.dto.OdgovorKorisnickePodrskeDTO;
import org.vs.dto.PorukaKorisnickePodrskeOdgovor;
import org.vs.dto.PorukaKorisnickePodrskeZahtjev;
import org.vs.service.PorukaKorisnickePodrskeService;

@RestController
@RequestMapping("/api/porukePodrske")
public class PorukaKorisnickePodrskeController {

	@Autowired
	private PorukaKorisnickePodrskeService porukaService;

	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("")
	public ResponseEntity<Void> dodajPoruku(@RequestBody PorukaKorisnickePodrskeZahtjev zahtjev) {
		porukaService.dodajPoruku(zahtjev);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping("")
	public ResponseEntity<Set<PorukaKorisnickePodrskeOdgovor>> prikaziSvePoruke() {
		Set<PorukaKorisnickePodrskeOdgovor> poruke = porukaService.prikaziSvePoruke();
		if (poruke.isEmpty())
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		else
			return new ResponseEntity<>(poruke, HttpStatus.OK);
	}

	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping("/{id}")
	public ResponseEntity<PorukaKorisnickePodrskeOdgovor> prikaziPoruku(@PathVariable(value = "id") Integer id) {
		PorukaKorisnickePodrskeOdgovor poruka = porukaService.prikaziPoruku(id);
		if (poruka != null) {
			return ResponseEntity.ok().body(poruka);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@CrossOrigin(origins = "http://localhost:8080")
	@PutMapping("/{id}/procitana")
	public ResponseEntity<Void> oznaciKaoProcitanu(@PathVariable(value = "id") Integer id) {
		PorukaKorisnickePodrskeOdgovor promjenjenaPoruka = porukaService.oznaciKaoProcitanu(id);
		if (promjenjenaPoruka == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.noContent().build();
	}

	@CrossOrigin(origins = "http://localhost:8080")
	@PutMapping("/{id}/odgovor")
	public ResponseEntity<Boolean> posaljiOdgovor(@PathVariable(value = "id") Integer id,
			@RequestBody OdgovorKorisnickePodrskeDTO odgovor) {
		boolean poslato = porukaService.posaljiOdgovor(id, odgovor);
		return ResponseEntity.ok(poslato);
	}

}
