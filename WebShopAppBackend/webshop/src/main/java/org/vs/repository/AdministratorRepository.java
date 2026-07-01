package org.vs.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vs.entity.Administrator;

public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

	Optional<Administrator> findByKorisnickoIme(String korisnickoIme);

	boolean existsByKorisnickoIme(String korisnickoIme);

}
