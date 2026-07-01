package org.vs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vs.entity.Korisnik;

public interface KorisnikRepository extends JpaRepository<Korisnik, Integer> {

	Optional<Korisnik> findByKorisnickoIme(String korisnickoIme);

	Optional<Korisnik> findByEmail(String email);

	boolean existsByKorisnickoIme(String korisnickoIme);

	boolean existsByEmail(String email);
}
