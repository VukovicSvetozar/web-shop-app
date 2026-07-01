package org.vs.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.vs.entity.*;

public interface ProizvodRepository extends JpaRepository<Proizvod, Integer> {

	Set<Proizvod> findByKategorijaId(Integer kategorijaId);

	Page<Proizvod> findByKategorijaContaining(Kategorija kategorija, Pageable pageable);

	Page<Proizvod> findByKategorija(Kategorija kategorija, Pageable pageable);

	Page<Proizvod> findByKategorija_Id(Integer kategorijaId, Pageable pageable);

	Page<Proizvod> findByKupovinaIsNull(Pageable pageable);

	Page<Proizvod> findByKategorijaAndKupovinaIsNull(Kategorija kategorija, Pageable pageable);

	Page<Proizvod> findByVlasnik(Korisnik vlasnik, Pageable pageable);

	Page<Proizvod> findByKupovina(Integer proizvodId, Pageable pageable);

	Page<Proizvod> findByKupovina_KupacId(Integer kupacId, Pageable pageable);

	List<Proizvod> findByKupovinaIsNull();

	List<Proizvod> findByKategorijaAndKupovinaIsNull(Kategorija kategorija);

	List<Proizvod> findByVlasnik(Korisnik vlasnik);
	
	List<Proizvod> findByKupovina_KupacId(Integer kupacId);

}
