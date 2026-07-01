package org.vs.service;

import java.util.*;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.vs.dto.KategorijaOdgovor;
import org.vs.dto.SpecifikacijaOdgovor;
import org.vs.entity.Kategorija;
import org.vs.entity.Specifikacija;
import org.vs.exception.EntitetNijePronadjenException;
import org.vs.repository.KategorijaRepository;
import org.vs.repository.SpecifikacijaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KategorijaService {

	private final KategorijaRepository kategorijaRepository;
	private final SpecifikacijaRepository specifikacijaRepository;
	private final ModelMapper modelMapper;

	public Set<KategorijaOdgovor> vratiSveKategorije() {
		List<Kategorija> kategorije = kategorijaRepository.findAll();
		return kategorije.stream().map(this::mapirajKategorijaUDTO).collect(Collectors.toSet());
	}

	public Set<SpecifikacijaOdgovor> vratiSveSpecifikacijePoIdKategorije(Integer kategorijaId) {
		if (!kategorijaRepository.existsById(kategorijaId)) {
			throw new EntitetNijePronadjenException("Kategorija sa id-em " + kategorijaId + " nije pronađena.");
		}
		Set<Specifikacija> specifikacije = specifikacijaRepository.findByKategorijaId(kategorijaId);
		return specifikacije.stream().map(this::mapirajSpecifikacijaUDTO).collect(Collectors.toSet());
	}

	private KategorijaOdgovor mapirajKategorijaUDTO(Kategorija kategorija) {
		KategorijaOdgovor dto = modelMapper.map(kategorija, KategorijaOdgovor.class);
		return dto;
	}

	private SpecifikacijaOdgovor mapirajSpecifikacijaUDTO(Specifikacija specifikacija) {
		SpecifikacijaOdgovor dto = modelMapper.map(specifikacija, SpecifikacijaOdgovor.class);
		return dto;
	}

}
