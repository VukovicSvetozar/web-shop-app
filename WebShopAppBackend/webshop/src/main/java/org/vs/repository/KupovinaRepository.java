package org.vs.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vs.entity.*;

public interface KupovinaRepository extends JpaRepository<Kupovina, Integer> {

	Set<Kupovina> findByKupac(Korisnik kupac);

}
