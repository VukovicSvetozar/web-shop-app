package org.vs.service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.vs.dto.AktivacijaZahtjev;
import org.vs.dto.AutentifikacijaAdministratorOdgovor;
import org.vs.dto.PrijavaOdgovor;
import org.vs.dto.PrijavaZahtjev;
import org.vs.dto.RegistracijaZahtjev;
import org.vs.entity.Administrator;
import org.vs.entity.Korisnik;
import org.vs.entity.Uloga;
import org.vs.exception.EntitetNijePronadjenException;
import org.vs.exception.NeispravniKredencijaliException;
import org.vs.exception.PogresanZahtjevException;
import org.vs.exception.SukobImenaException;
import org.vs.repository.AdministratorRepository;
import org.vs.repository.KorisnikRepository;
import org.vs.security.JwtService;
import org.vs.security.SigurniKorisnik;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutentifikacijaService {

	private final KorisnikRepository korisnikRepository;
	private final AdministratorRepository administratorRepository;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;
	private final MailService mailService;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public void registracija(RegistracijaZahtjev zahtjev) {
		if (korisnikRepository.existsByKorisnickoIme(zahtjev.getKorisnickoIme()))
			throw new SukobImenaException("Korisnicko ime se vec koristi.");
		if (korisnikRepository.existsByEmail(zahtjev.getEmail()))
			throw new SukobImenaException("Email adresa se vec koristi.");
		Korisnik korisnik = modelMapper.map(zahtjev, Korisnik.class);
		korisnik.setLozinka(passwordEncoder.encode(korisnik.getLozinka()));
		korisnik.setDatumPristupa(LocalDate.now().toString());
		String pin = kreirajPin();
		korisnik.setPin(pin);
		korisnik.setUloga(Uloga.REGISTROVANI.name());
		korisnikRepository.saveAndFlush(korisnik);
		if (!mailService.posaljiPin(zahtjev.getEmail(), pin)) {
			throw new PogresanZahtjevException("Slanje email-a nije bilo uspjesno.");
		}
	}

	public boolean provjeriDostupnostKorisnickogImena(String korisnickoIme) {
		if ("dmnstrtra".equals(korisnickoIme) || "dmnstrtrs".equals(korisnickoIme))
			return false;
		Optional<Korisnik> korisnik = korisnikRepository.findByKorisnickoIme(korisnickoIme);
		return !korisnik.isPresent();
	}

	public boolean provjeriDostupnostEmaila(String email) {
		Optional<Korisnik> korisnik = korisnikRepository.findByEmail(email);
		return !korisnik.isPresent();
	}

	public PrijavaOdgovor aktivacija(AktivacijaZahtjev zahtjev) {
		Korisnik korisnik = korisnikRepository.findByKorisnickoIme(zahtjev.getKorisnickoIme())
				.orElseThrow(() -> new EntitetNijePronadjenException("Korisnik nije pronađen"));
		if (!korisnik.getPin().equals(String.valueOf(zahtjev.getPin())))
			throw new NeispravniKredencijaliException("Pogresan pin.");
		var sigurniKorisnik = modelMapper.map(korisnik, SigurniKorisnik.class);
		var jwtToken = jwtService.kreirajToken(sigurniKorisnik);
		var refreshJwtTokens = jwtService.kreirajRefreshToken(sigurniKorisnik);
		korisnik.setUloga(Uloga.VERIFIKOVANI.name());
		korisnikRepository.saveAndFlush(korisnik);
		return PrijavaOdgovor.builder().korisnickoIme(zahtjev.getKorisnickoIme()).avatarUrl(korisnik.getAvatarUrl())
				.jwtToken(jwtToken).refreshJwtToken(refreshJwtTokens).build();
	}

	public PrijavaOdgovor prijava(PrijavaZahtjev zahtjev) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(zahtjev.getKorisnickoIme(), zahtjev.getLozinka()));
		Korisnik korisnik = korisnikRepository.findByKorisnickoIme(zahtjev.getKorisnickoIme())
				.orElseThrow(() -> new EntitetNijePronadjenException("Korisnik nije pronađen"));
		if (Uloga.REGISTROVANI.name().equals(korisnik.getUloga()))
			return null;
		if (Uloga.VERIFIKOVANI.name().equals(korisnik.getUloga())) {
			var sigurniKorisnik = modelMapper.map(korisnik, SigurniKorisnik.class);
			var jwtToken = jwtService.kreirajToken(sigurniKorisnik);
			var refreshJwtTokens = jwtService.kreirajRefreshToken(sigurniKorisnik);
			korisnikRepository.saveAndFlush(korisnik);
			return PrijavaOdgovor.builder().korisnickoIme(korisnik.getKorisnickoIme())
					.avatarUrl(korisnik.getAvatarUrl()).jwtToken(jwtToken).refreshJwtToken(refreshJwtTokens).build();
		}
		throw new PogresanZahtjevException("Doslo je do greske tokom prijave.");
	}

	private String kreirajPin() {
		SecureRandom secureRandom = new SecureRandom();
		int pin = secureRandom.nextInt(9000) + 1000;
		return String.valueOf(pin);
	}

	public AutentifikacijaAdministratorOdgovor autentifikacijaAdministrator(PrijavaZahtjev zahtjev) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(zahtjev.getKorisnickoIme(), zahtjev.getLozinka()));
		Administrator administrator = administratorRepository.findByKorisnickoIme(zahtjev.getKorisnickoIme())
				.orElseThrow(() -> new EntitetNijePronadjenException("Korisnik nije pronađen"));
		var sigurniKorisnik = modelMapper.map(administrator, SigurniKorisnik.class);
		var jwtToken = jwtService.kreirajToken(sigurniKorisnik);
		return AutentifikacijaAdministratorOdgovor.builder().jwtToken(jwtToken).build();
	}

}
