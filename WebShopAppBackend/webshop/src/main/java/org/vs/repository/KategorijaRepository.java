package org.vs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.vs.entity.Kategorija;

public interface KategorijaRepository extends JpaRepository<Kategorija, Integer> {
}
