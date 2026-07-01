package org.vs.security;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.vs.entity.Administrator;
import org.vs.entity.Korisnik;
import org.vs.exception.EntitetNijePronadjenException;
import org.vs.exception.NeispravniKredencijaliException;
import org.vs.repository.AdministratorRepository;
import org.vs.repository.KorisnikRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final KorisnikRepository korisnikRepository;
	private final AdministratorRepository administratorRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String korisnickoIme = authentication.getName();
		String lozinka = authentication.getCredentials().toString();
		if ("dmnstrtra".equals(korisnickoIme) || "dmnstrtrs".equals(korisnickoIme)) {
			Administrator admin = administratorRepository.findByKorisnickoIme(korisnickoIme)
					.orElseThrow(() -> new EntitetNijePronadjenException("Korisnik nije pronađen"));
			if (!passwordEncoder.matches(lozinka, admin.getLozinka())) {
				throw new NeispravniKredencijaliException("Neispravna lozinka");
			}
			var sigurniAdministrator = modelMapper.map(admin, SigurniKorisnik.class);
			return new UsernamePasswordAuthenticationToken(sigurniAdministrator, null,
					sigurniAdministrator.getAuthorities());
		} else {
			Korisnik korisnik = korisnikRepository.findByKorisnickoIme(korisnickoIme)
					.orElseThrow(() -> new EntitetNijePronadjenException("Korisnik nije pronađen"));
			if (!passwordEncoder.matches(lozinka, korisnik.getLozinka())) {
				throw new NeispravniKredencijaliException("Neispravna lozinka");
			}
			var sigurniKorisnik = modelMapper.map(korisnik, SigurniKorisnik.class);
			return new UsernamePasswordAuthenticationToken(sigurniKorisnik, null, sigurniKorisnik.getAuthorities());
		}
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
