package org.vs.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.vs.dto.OdgovorKorisnickePodrskeDTO;
import org.vs.dto.PorukaKorisnickePodrskeOdgovor;
import org.vs.dto.PorukaKorisnickePodrskeZahtjev;
import org.vs.entity.Korisnik;
import org.vs.entity.PorukaKorisnickePodrske;
import org.vs.exception.EntitetNijePronadjenException;
import org.vs.repository.KorisnikRepository;
import org.vs.repository.PorukaKorisnickePodrskeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PorukaKorisnickePodrskeService {

	private final KorisnikRepository korisnikRepository;
	private final PorukaKorisnickePodrskeRepository porukaRepository;
	private final ModelMapper modelMapper;
	private final JavaMailSender javaMailSender;

	public void dodajPoruku(PorukaKorisnickePodrskeZahtjev zahtjev) {
		Korisnik korisnik = korisnikRepository.findByKorisnickoIme(zahtjev.getKorisnickoIme())
				.orElseThrow(() -> new EntitetNijePronadjenException("Korisnik nije pronađen"));
		PorukaKorisnickePodrske poruka = new PorukaKorisnickePodrske();
		poruka.setNaslovPoruke(zahtjev.getNaslovPoruke());
		poruka.setTekstPoruke(zahtjev.getTekstPoruke());
		poruka.setVrijemePoruke(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString());
		poruka.setProcitana(false);
		poruka.setKorisnik(korisnik);
		porukaRepository.saveAndFlush(poruka);
	}

	public Set<PorukaKorisnickePodrskeOdgovor> prikaziSvePoruke() {
		List<PorukaKorisnickePodrske> poruke = porukaRepository.findAll();
		return poruke.stream().map(this::mapirajUDTO).collect(Collectors.toSet());
	}

	public PorukaKorisnickePodrskeOdgovor prikaziPoruku(Integer id) {
		Optional<PorukaKorisnickePodrske> porukaOptional = porukaRepository.findById(id);
		if (porukaOptional.isPresent()) {
			return modelMapper.map(porukaOptional.get(), PorukaKorisnickePodrskeOdgovor.class);
		}
		return null;
	}

	public PorukaKorisnickePodrskeOdgovor oznaciKaoProcitanu(Integer id) {
		Optional<PorukaKorisnickePodrske> porukaOptional = porukaRepository.findById(id);
		if (porukaOptional.isPresent()) {
			PorukaKorisnickePodrske poruka = porukaOptional.get();
			PorukaKorisnickePodrskeOdgovor porukaDTO = modelMapper.map(porukaOptional.get(),
					PorukaKorisnickePodrskeOdgovor.class);
			poruka.setProcitana(true);
			porukaRepository.save(poruka);
			return porukaDTO;
		}
		return null;
	}

	public boolean posaljiOdgovor(Integer id, OdgovorKorisnickePodrskeDTO odgovor) {
		boolean poslato = false;
		SimpleMailMessage smm = new SimpleMailMessage();
		smm.setFrom("svetozarvukovic.server@gmail.com");
		smm.setTo(odgovor.getEmail());
		smm.setSubject(odgovor.getNaslov());
		smm.setText(odgovor.getTekst());

		try {
			javaMailSender.send(smm);
			Optional<PorukaKorisnickePodrske> optionalPoruka = porukaRepository.findById(id);
			if (optionalPoruka.isPresent()) {
				PorukaKorisnickePodrske poruka = optionalPoruka.get();
				poruka.setNaslovOdgovor(odgovor.getNaslov());
				poruka.setTekstOdgovor(odgovor.getTekst());
				poruka.setVrijemeOdgovor(LocalDateTime.now().toString());
				porukaRepository.save(poruka);
				poslato = true;
			}
		} catch (MailException ex) {
			poslato = false;
		}
		return poslato;
	}

	private PorukaKorisnickePodrskeOdgovor mapirajUDTO(PorukaKorisnickePodrske poruka) {
		PorukaKorisnickePodrskeOdgovor dto = modelMapper.map(poruka, PorukaKorisnickePodrskeOdgovor.class);
		dto.setKorisnickoIme(poruka.getKorisnik().getKorisnickoIme());
		dto.setEmail(poruka.getKorisnik().getEmail());
		return dto;
	}

}
