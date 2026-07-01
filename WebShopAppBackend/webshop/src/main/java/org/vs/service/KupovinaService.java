package org.vs.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;
import org.vs.dto.KupovinaZahtjev;
import org.vs.entity.Korisnik;
import org.vs.entity.Kupovina;
import org.vs.entity.Placanje;
import org.vs.entity.Proizvod;
import org.vs.exception.EntitetNijePronadjenException;
import org.vs.exception.PogresanZahtjevException;
import org.vs.repository.KorisnikRepository;
import org.vs.repository.KupovinaRepository;
import org.vs.repository.PlacanjeRepository;
import org.vs.repository.ProizvodRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KupovinaService {

	private final PlacanjeRepository placanjeRepository;
	private final KupovinaRepository kupovinaRepository;
	private final ProizvodRepository proizvodRepository;
	private final KorisnikRepository korisnikRepository;

	public void kupi(KupovinaZahtjev zahtjev) {
		if ((zahtjev.getNazivKurirskeSluzbe() == null && zahtjev.getBrojKartice() == null)
				|| (zahtjev.getNazivKurirskeSluzbe() != null && zahtjev.getBrojKartice() != null))
			throw new PogresanZahtjevException("Morate unijeti ili naziv kurirske službe ili broj kartice.");
		Proizvod proizvod = proizvodRepository.findById(zahtjev.getProizvodId())
				.orElseThrow(() -> new EntitetNijePronadjenException(
						"Proizvod sa id: " + zahtjev.getProizvodId() + " nije pronadjen"));
		Korisnik kupac = korisnikRepository.findByKorisnickoIme(zahtjev.getKorisnickoImeKupca())
				.orElseThrow(() -> new EntitetNijePronadjenException("Kupac nije pronađen"));
		Korisnik prodavac = korisnikRepository.findById(proizvod.getVlasnik().getId())
				.orElseThrow(() -> new EntitetNijePronadjenException("Prodavac nije pronađen"));
		if (proizvod.getVlasnik().getId() == kupac.getId())
			throw new PogresanZahtjevException("Ne mozete kupiti sopstveni proizvod.");
		if (proizvod.getKupovina() != null)
			throw new PogresanZahtjevException("Proizvod je vec kupljen.");

		Placanje placanje = Placanje.builder().nazivKurirskeSluzbe(zahtjev.getNazivKurirskeSluzbe())
				.brojKartice(zahtjev.getBrojKartice()).build();
		placanjeRepository.save(placanje);
		Kupovina kupovina = Kupovina.builder()
				.vrijemeKupovine(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString()).placanje(placanje)
				.proizvod(proizvod).prodavac(prodavac).kupac(kupac)	.build();
		kupovinaRepository.save(kupovina);
	}

}
