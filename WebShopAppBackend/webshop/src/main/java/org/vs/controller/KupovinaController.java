package org.vs.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.vs.dto.KupovinaZahtjev;
import org.vs.service.KupovinaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kupovina")
public class KupovinaController {

	private final KupovinaService kupovinaService;

	@PostMapping("")
	public ResponseEntity<Void> kupi(@RequestBody @Valid KupovinaZahtjev zahtjev) {
		kupovinaService.kupi(zahtjev);
		return ResponseEntity.ok().build();
	}

}
