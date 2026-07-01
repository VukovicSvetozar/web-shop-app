package org.vs.repository;

import java.util.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.vs.entity.Proizvod;
import org.vs.entity.ProizvodKomentar;

public interface ProizvodKomentarRepository extends JpaRepository<ProizvodKomentar, Integer> {

	List<ProizvodKomentar> findByProizvodId(Integer proizvodId);
	
	Page<ProizvodKomentar> findByProizvod(Proizvod proizvod, Pageable pageable);

}
