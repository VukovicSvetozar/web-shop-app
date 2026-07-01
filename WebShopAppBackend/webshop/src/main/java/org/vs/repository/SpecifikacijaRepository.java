package org.vs.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vs.entity.Specifikacija;

public interface SpecifikacijaRepository extends JpaRepository<Specifikacija, Integer> {

	Set<Specifikacija> findByKategorijaId(Integer kategorijaId);
}
