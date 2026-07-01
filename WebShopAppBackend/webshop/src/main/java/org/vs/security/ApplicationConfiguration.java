package org.vs.security;

import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vs.entity.Administrator;
import org.vs.entity.Korisnik;
import org.vs.exception.EntitetNijePronadjenException;
import org.vs.repository.AdministratorRepository;
import org.vs.repository.KorisnikRepository;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class ApplicationConfiguration {

	private final KorisnikRepository korisnikRepository;
	private final AdministratorRepository administratorRepository;
	private final ModelMapper modelMapper;

	@Bean
	UserDetailsService userDetailsService() {
		return korisnickoIme -> {
			if ("dmnstrtra".equals(korisnickoIme) || "dmnstrtrs".equals(korisnickoIme)) {
				Administrator admin = administratorRepository.findByKorisnickoIme(korisnickoIme).get();
				return modelMapper.map(admin, SigurniKorisnik.class);
			} else {
				Korisnik korisnik = korisnikRepository.findByKorisnickoIme(korisnickoIme)
						.orElseThrow(() -> new EntitetNijePronadjenException("Korisnik nije pronadjen."));
				return modelMapper.map(korisnik, SigurniKorisnik.class);
			}
		};
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration AuthenticationConfiguration)
			throws Exception {
		return AuthenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
