package org.vs.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.vs.controller.ProizvodController;
import org.vs.dto.PretragaZahtjev;
import org.vs.dto.ProizvodDetaljiOdgovor;
import org.vs.dto.ProizvodKarticaOdgovor;
import org.vs.dto.ProizvodKomentarOdgovor;
import org.vs.dto.ProizvodKomentarZahtjev;
import org.vs.dto.ProizvodSpecifikacijaZahtjev;
import org.vs.dto.ProizvodStatistikaOdgovor;
import org.vs.dto.ProizvodZahtjev;
import org.vs.dto.SlikaInfo;
import org.vs.entity.Kategorija;
import org.vs.entity.Korisnik;
import org.vs.entity.Proizvod;
import org.vs.entity.ProizvodKomentar;
import org.vs.entity.ProizvodSpecifikacija;
import org.vs.entity.ProizvodStatus;
import org.vs.entity.Specifikacija;
import org.vs.exception.EntitetNijePronadjenException;
import org.vs.exception.PogresanZahtjevException;
import org.vs.repository.KategorijaRepository;
import org.vs.repository.KorisnikRepository;
import org.vs.repository.ProizvodKomentarRepository;
import org.vs.repository.ProizvodRepository;
import org.vs.repository.ProizvodSpecifikacijaRepository;
import org.vs.repository.SpecifikacijaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProizvodService {

	private final ProizvodRepository proizvodRepository;
	private final KorisnikRepository korisnikRepository;
	private final KategorijaRepository kategorijaRepository;
	private final SpecifikacijaRepository specifikacijaRepository;
	private final ProizvodSpecifikacijaRepository proizvodSpecifikacijaRepository;
	private final ProizvodKomentarRepository proizvodKomentarRepository;
	private final SlikaService slikaService;
	private final ModelMapper modelMapper;

	private Sort.Direction getSortDirection(String direction) {
		if (direction.equals("r")) {
			return Sort.Direction.ASC;
		} else if (direction.equals("o")) {
			return Sort.Direction.DESC;
		}
		return Sort.Direction.ASC;
	}

	public Map<String, Object> vratiSveKarticeProizvoda(@RequestParam(required = false) Integer kategorijaId,
			@RequestParam(defaultValue = "0") int trenutnaStranica, @RequestParam(defaultValue = "9") int brojElemenata,
			@RequestParam(defaultValue = "id,o") String[] sortiranje) {

		List<Order> orders = new ArrayList<Order>();
		Map<String, Object> odgovor = new HashMap<>();

		if (sortiranje[0].contains(",")) {
			for (String sortOrder : sortiranje) {
				String[] _sort = sortOrder.split(",");
				orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
			}
		} else {
			orders.add(new Order(getSortDirection(sortiranje[1]), sortiranje[0]));
		}

		List<Proizvod> listaProizvoda = new ArrayList<Proizvod>();
		List<ProizvodKarticaOdgovor> listaKarticaProizvoda = new ArrayList<ProizvodKarticaOdgovor>();

		Pageable pagingSort = PageRequest.of(trenutnaStranica, brojElemenata, Sort.by(orders));
		Page<Proizvod> pageKarticaProizvodi;

		if (kategorijaId == null) {
			pageKarticaProizvodi = proizvodRepository.findByKupovinaIsNull(pagingSort);

		} else {
			Kategorija kategorija = kategorijaRepository.findById(kategorijaId)
					.orElseThrow(() -> new EntitetNijePronadjenException("Kategorija nije pronadjena."));
			pageKarticaProizvodi = proizvodRepository.findByKategorijaAndKupovinaIsNull(kategorija, pagingSort);
		}

		listaProizvoda = pageKarticaProizvodi.getContent();
		listaKarticaProizvoda = vratiSveKarticeProizvoda(listaProizvoda);
		odgovor.put("kartice", listaKarticaProizvoda);
		odgovor.put("trenutnaStranica", pageKarticaProizvodi.getNumber());
		odgovor.put("ukupnoElemenata", pageKarticaProizvodi.getTotalElements());
		odgovor.put("brojStranica", pageKarticaProizvodi.getTotalPages());
		return odgovor;
	}

	public Map<String, Object> vratiKarticeProizvodaKorisnika(String korisnickoIme,
			@RequestParam(defaultValue = "0") int trenutnaStranica, @RequestParam(defaultValue = "9") int brojElemenata,
			@RequestParam(defaultValue = "id,o") String[] sortiranje) {

		List<Order> orders = new ArrayList<Order>();
		Map<String, Object> odgovor = new HashMap<>();

		if (sortiranje[0].contains(",")) {
			for (String sortOrder : sortiranje) {
				String[] _sort = sortOrder.split(",");
				orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
			}
		} else {
			orders.add(new Order(getSortDirection(sortiranje[1]), sortiranje[0]));
		}

		List<Proizvod> listaProizvoda = new ArrayList<Proizvod>();

		Pageable pagingSort = PageRequest.of(trenutnaStranica, brojElemenata, Sort.by(orders));
		Page<Proizvod> pageKarticaProizvodi;

		Korisnik vlasnik = korisnikRepository.findByKorisnickoIme(korisnickoIme)
				.orElseThrow(() -> new EntitetNijePronadjenException("Korisnik nije pronadjen."));
		pageKarticaProizvodi = proizvodRepository.findByVlasnik(vlasnik, pagingSort);
		listaProizvoda = pageKarticaProizvodi.getContent();

		List<Proizvod> listaAktivnihProizvoda = listaProizvoda.stream()
				.filter(proizvod -> proizvod.getKupovina() == null).collect(Collectors.toList());
		List<Proizvod> listaZavrsenihProizvoda = listaProizvoda.stream()
				.filter(proizvod -> proizvod.getKupovina() != null).collect(Collectors.toList());

		List<ProizvodKarticaOdgovor> listaAktivnihKarticaProizvoda = vratiSveKarticeProizvoda(listaAktivnihProizvoda);
		List<ProizvodKarticaOdgovor> listaZavrsenihKarticaProizvoda = vratiSveKarticeProizvoda(listaZavrsenihProizvoda);

		odgovor.put("aktivneKartice", listaAktivnihKarticaProizvoda);
		odgovor.put("zavrseneKartice", listaZavrsenihKarticaProizvoda);
		odgovor.put("trenutnaStranica", pageKarticaProizvodi.getNumber());
		odgovor.put("ukupnoElemenata", pageKarticaProizvodi.getTotalElements());
		odgovor.put("brojStranica", pageKarticaProizvodi.getTotalPages());
		return odgovor;
	}

	public Map<String, Object> vratiAktivneKarticeProizvodaKorisnika(String korisnickoIme,
			@RequestParam(defaultValue = "0") int trenutnaStranica,
			@RequestParam(defaultValue = "4") int brojElemenata) {

		Map<String, Object> odgovor = new HashMap<>();
		List<Proizvod> listaProizvoda = new ArrayList<Proizvod>();

		Korisnik vlasnik = korisnikRepository.findByKorisnickoIme(korisnickoIme)
				.orElseThrow(() -> new EntitetNijePronadjenException("Korisnik nije pronadjen."));
		listaProizvoda = proizvodRepository.findByVlasnik(vlasnik);
		List<Proizvod> listaAktivnihProizvoda = listaProizvoda.stream()
				.filter(proizvod -> proizvod.getKupovina() == null).collect(Collectors.toList());

		int indeksPocetka = trenutnaStranica * brojElemenata;
		int indeksKraja = Math.min((trenutnaStranica + 1) * brojElemenata, listaAktivnihProizvoda.size());
		int ukupnoElemenata = listaAktivnihProizvoda.size();
		int ukupanBrojStranica = ukupnoElemenata / brojElemenata + ((ukupnoElemenata % brojElemenata != 0) ? 1 : 0);
		if (indeksPocetka < indeksKraja)
			listaAktivnihProizvoda = listaAktivnihProizvoda.subList(indeksPocetka, indeksKraja);

		List<ProizvodKarticaOdgovor> listaAktivnihKarticaProizvoda = vratiSveKarticeProizvoda(listaAktivnihProizvoda);

		odgovor.put("aktivneKartice", listaAktivnihKarticaProizvoda);
		odgovor.put("trenutnaStranica", trenutnaStranica);
		odgovor.put("ukupnoElemenata", ukupnoElemenata);
		odgovor.put("brojStranica", ukupanBrojStranica);
		return odgovor;
	}

	public Map<String, Object> vratiZavrseneKarticeProizvodaKorisnika(String korisnickoIme,
			@RequestParam(defaultValue = "0") int trenutnaStranica,
			@RequestParam(defaultValue = "9") int brojElemenata) {

		Map<String, Object> odgovor = new HashMap<>();
		List<Proizvod> listaProizvoda = new ArrayList<Proizvod>();

		Korisnik vlasnik = korisnikRepository.findByKorisnickoIme(korisnickoIme)
				.orElseThrow(() -> new EntitetNijePronadjenException("Korisnik nije pronadjen."));
		listaProizvoda = proizvodRepository.findByVlasnik(vlasnik);

		List<Proizvod> listaZavrsenihProizvoda = listaProizvoda.stream()
				.filter(proizvod -> proizvod.getKupovina() != null).collect(Collectors.toList());

		int indeksPocetka = trenutnaStranica * brojElemenata;
		int indeksKraja = Math.min((trenutnaStranica + 1) * brojElemenata, listaZavrsenihProizvoda.size());
		int ukupnoElemenata = listaZavrsenihProizvoda.size();
		int ukupanBrojStranica = ukupnoElemenata / brojElemenata + ((ukupnoElemenata % brojElemenata != 0) ? 1 : 0);
		if (indeksPocetka < indeksKraja)
			listaZavrsenihProizvoda = listaZavrsenihProizvoda.subList(indeksPocetka, indeksKraja);

		List<ProizvodKarticaOdgovor> listaZavrsenihKarticaProizvoda = vratiSveKarticeProizvoda(listaZavrsenihProizvoda);

		odgovor.put("zavrseneKartice", listaZavrsenihKarticaProizvoda);
		odgovor.put("trenutnaStranica", trenutnaStranica);
		odgovor.put("ukupnoElemenata", ukupnoElemenata);
		odgovor.put("brojStranica", ukupanBrojStranica);
		return odgovor;
	}

	public Map<String, Object> vratiKarticeKupljenihProizvodaKorisnika(String korisnickoIme,
			@RequestParam(defaultValue = "0") int trenutnaStranica, @RequestParam(defaultValue = "4") int brojElemenata,
			@RequestParam(defaultValue = "id,o") String[] sortiranje) {

		List<Order> orders = new ArrayList<Order>();
		Map<String, Object> odgovor = new HashMap<>();

		if (sortiranje[0].contains(",")) {
			for (String sortOrder : sortiranje) {
				String[] _sort = sortOrder.split(",");
				orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
			}
		} else {
			orders.add(new Order(getSortDirection(sortiranje[1]), sortiranje[0]));
		}

		List<Proizvod> listaProizvoda = new ArrayList<Proizvod>();

		Pageable pagingSort = PageRequest.of(trenutnaStranica, brojElemenata, Sort.by(orders));
		Page<Proizvod> pageKarticaProizvodi;

		Korisnik kupac = korisnikRepository.findByKorisnickoIme(korisnickoIme)
				.orElseThrow(() -> new EntitetNijePronadjenException("Korisnik nije pronadjen."));
		pageKarticaProizvodi = proizvodRepository.findByKupovina_KupacId(kupac.getId(), pagingSort);
		listaProizvoda = pageKarticaProizvodi.getContent();
		List<ProizvodKarticaOdgovor> listaKupljenihKarticaProizvoda = vratiSveKarticeProizvoda(listaProizvoda);

		odgovor.put("kupljeneKartice", listaKupljenihKarticaProizvoda);
		odgovor.put("trenutnaStranica", pageKarticaProizvodi.getNumber());
		odgovor.put("ukupnoElemenata", pageKarticaProizvodi.getTotalElements());
		odgovor.put("brojStranica", pageKarticaProizvodi.getTotalPages());
		return odgovor;
	}

	private List<ProizvodKarticaOdgovor> vratiSveKarticeProizvoda(List<Proizvod> kartice) {
		return kartice.stream().map(this::mapirajProizvodKarticeUDTO).collect(Collectors.toList());
	}

	public ProizvodStatistikaOdgovor vratiStatistiku(String korisnickoIme) {
		Korisnik korisnik = korisnikRepository.findByKorisnickoIme(korisnickoIme)
				.orElseThrow(() -> new EntitetNijePronadjenException("Korisnik nije pronadjen."));
		List<Proizvod> listaProizvoda = proizvodRepository.findByVlasnik(korisnik);
		List<Proizvod> listaAktivnihProizvoda = listaProizvoda.stream()
				.filter(proizvod -> proizvod.getKupovina() == null).collect(Collectors.toList());
		List<Proizvod> listaZavrsenihProizvoda = listaProizvoda.stream()
				.filter(proizvod -> proizvod.getKupovina() != null).collect(Collectors.toList());
		List<Proizvod> listaKupljenihProizvoda = proizvodRepository.findByKupovina_KupacId(korisnik.getId());
		ProizvodStatistikaOdgovor statistika = new ProizvodStatistikaOdgovor(listaAktivnihProizvoda.size(),
				listaZavrsenihProizvoda.size(), listaKupljenihProizvoda.size());
		return statistika;
	}

	public Map<String, Object> pretragaKarticaProizvoda(PretragaZahtjev zahtjev,
			@RequestParam(defaultValue = "0") int trenutnaStranica, @RequestParam(defaultValue = "9") int brojElemenata,
			@RequestParam(defaultValue = "id,o") String[] sortiranje) {

		List<Order> orders = new ArrayList<Order>();
		Map<String, Object> odgovor = new HashMap<>();
		try {
			if (sortiranje[0].contains(",")) {
				for (String sortOrder : sortiranje) {
					String[] _sort = sortOrder.split(",");
					orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
				}
			} else {
				orders.add(new Order(getSortDirection(sortiranje[1]), sortiranje[0]));
			}

			List<Proizvod> listaProizvoda = new ArrayList<Proizvod>();

			if (zahtjev.getKategorijaId() == null) {
				listaProizvoda = proizvodRepository.findByKupovinaIsNull();

			} else {
				Kategorija kategorija = kategorijaRepository.findById(zahtjev.getKategorijaId())
						.orElseThrow(() -> new EntitetNijePronadjenException("Kategorija nije pronadjena."));
				listaProizvoda = proizvodRepository.findByKategorijaAndKupovinaIsNull(kategorija);
			}

			if (zahtjev.getKorisnickoIme() != null) {
				listaProizvoda = listaProizvoda.stream()
						.filter(proizvod -> proizvod.getVlasnik().getKorisnickoIme() != null
								&& proizvod.getVlasnik().getKorisnickoIme().equals(zahtjev.getKorisnickoIme()))
						.collect(Collectors.toList());
			}

			if (zahtjev.getKratakNaslov() != null) {
				listaProizvoda = listaProizvoda.stream()
						.filter(proizvod -> proizvod.getKratakNaslov() != null && proizvod.getKratakNaslov()
								.toLowerCase().contains(zahtjev.getKratakNaslov().toLowerCase()))
						.collect(Collectors.toList());
			}

			if (zahtjev.getLokacija() != null) {
				listaProizvoda = listaProizvoda.stream().filter(proizvod -> proizvod.getLokacija() != null
						&& proizvod.getLokacija().equals(zahtjev.getLokacija())).collect(Collectors.toList());
			}

			if (zahtjev.getMinCijena() != null && zahtjev.getMaxCijena() != null) {
				listaProizvoda = listaProizvoda.stream()
						.filter(proizvod -> proizvod.getCijena() >= zahtjev.getMinCijena()
								&& proizvod.getCijena() <= zahtjev.getMaxCijena())
						.collect(Collectors.toList());
			}

			if (zahtjev.getDatumOd() != null && zahtjev.getDatumDo() != null) {
				LocalDate dateOd = LocalDate.parse(zahtjev.getDatumOd());
				LocalDate dateDo = LocalDate.parse(zahtjev.getDatumDo());
				listaProizvoda = listaProizvoda.stream().filter(proizvod -> {
					LocalDate proizvodDatum = LocalDateTime.parse(proizvod.getVrijemeObjave()).toLocalDate();
					return proizvodDatum.isAfter(dateOd.minusDays(1)) && proizvodDatum.isBefore(dateDo.plusDays(1));
				}).collect(Collectors.toList());
			}

			boolean pretragaStanja = false;
			List<Proizvod> polovniProizvodi = new ArrayList<Proizvod>();
			if (zahtjev.isPolovan()) {
				polovniProizvodi = listaProizvoda.stream().filter(proizvod -> proizvod.isPolovan())
						.collect(Collectors.toList());
				pretragaStanja = true;
			}
			List<Proizvod> noviProizvodi = new ArrayList<Proizvod>();
			if (zahtjev.isNov()) {
				noviProizvodi = listaProizvoda.stream().filter(proizvod -> !proizvod.isPolovan())
						.collect(Collectors.toList());
				pretragaStanja = true;
			}
			if (pretragaStanja) {
				listaProizvoda.isEmpty();
				listaProizvoda = Stream.concat(polovniProizvodi.stream(), noviProizvodi.stream()).toList();
			}

			int indeksPocetka = trenutnaStranica * brojElemenata;
			int indeksKraja = Math.min((trenutnaStranica + 1) * brojElemenata, listaProizvoda.size());
			int ukupnoElemenata = listaProizvoda.size();
			int ukupanBrojStranica = ukupnoElemenata / brojElemenata + ((ukupnoElemenata % brojElemenata != 0) ? 1 : 0);
			if (indeksPocetka < indeksKraja)
				listaProizvoda = listaProizvoda.subList(indeksPocetka, indeksKraja);

			List<ProizvodKarticaOdgovor> listaKarticaProizvoda = vratiSveKarticeProizvoda(listaProizvoda);

			odgovor.put("pronadjeneKartice", listaKarticaProizvoda);
			odgovor.put("trenutnaStranica", trenutnaStranica);
			odgovor.put("ukupnoElemenata", ukupnoElemenata);
			odgovor.put("brojStranica", ukupanBrojStranica);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return odgovor;
	}

	public ProizvodDetaljiOdgovor vratiProizvodPoId(Integer id) {
		Proizvod proizvod = proizvodRepository.findById(id)
				.orElseThrow(() -> new EntitetNijePronadjenException("Proizvod sa id-em " + id + " nije pronađen."));
		ProizvodDetaljiOdgovor proizvodDetalji = modelMapper.map(proizvod, ProizvodDetaljiOdgovor.class);
		Korisnik korisnik = korisnikRepository.findById(proizvod.getVlasnik().getId())
				.orElseThrow(() -> new EntitetNijePronadjenException("Korisnik nije pronađen"));
		proizvodDetalji.setAvatarUrl(korisnik.getAvatarUrl());
		proizvodDetalji.setDatumPristupa(korisnik.getDatumPristupa());
		proizvodDetalji.setKorisnickoIme(korisnik.getKorisnickoIme());
		proizvodDetalji.setGrad(korisnik.getGrad());
		proizvodDetalji.setEmail(korisnik.getEmail());
		proizvodDetalji.setBrojTelefona(korisnik.getBrojTelefona());
		proizvodDetalji.setNazivKategorije(proizvod.getKategorija().getNaziv());

		int kategorijaId = proizvod.getKategorija().getId();
		Set<Specifikacija> specifikacije = specifikacijaRepository.findByKategorijaId(kategorijaId);
		List<ProizvodSpecifikacija> listaProizvodSpecifikacija = proizvodSpecifikacijaRepository.findAll();
		Map<String, String> specificneOsobine = new HashMap<>();
		specifikacije.forEach(s -> {
			Optional<ProizvodSpecifikacija> odgovarajucaProizvodSpecifikacija = listaProizvodSpecifikacija.stream()
					.filter(ps -> ps.getId().getProizvod().getId() == proizvod.getId()
							&& ps.getId().getSpecifikacija().getId() == s.getId())
					.findFirst();
			odgovarajucaProizvodSpecifikacija.ifPresent(ps -> {
				specificneOsobine.put(s.getNaziv(), ps.getVrijednost());
			});
		});
		proizvodDetalji.setSpecificneOsobine(specificneOsobine);
		proizvodDetalji.setSlikeProizvodaUrl(ucitajSlikeProizvoda(id));

		return proizvodDetalji;
	}

	public Integer dodajProizvod(ProizvodZahtjev zahtjev) {
		if (!kategorijaRepository.existsById(zahtjev.getKategorijaId())) {
			throw new EntitetNijePronadjenException(
					"Kategorija sa id-em " + zahtjev.getKategorijaId() + " nije pronađen.");
		}
		Korisnik vlasnik = korisnikRepository.findByKorisnickoIme(zahtjev.getKorisnickoIme())
				.orElseThrow(() -> new EntitetNijePronadjenException("Korisnik nije pronađen"));
		Proizvod proizvod = modelMapper.map(zahtjev, Proizvod.class);
		proizvod.setStatus(ProizvodStatus.AKTIVAN.name());
		proizvod.setVrijemeObjave(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString());
		proizvod.setVlasnik(vlasnik);
		proizvod.setId(null);
		Proizvod dodatiProizvod = proizvodRepository.saveAndFlush(proizvod);
		return dodatiProizvod.getId();
	}

	public void odustaniOdKreiranjaProizvoda(Integer id) {
		Proizvod proizvod = proizvodRepository.findById(id)
				.orElseThrow(() -> new EntitetNijePronadjenException("Proizvod nije pronađen."));
		proizvodRepository.delete(proizvod);
	}

	public void obrisiProizvod(String korisnickoIme, Integer id) {
		Proizvod proizvod = proizvodRepository.findById(id)
				.orElseThrow(() -> new EntitetNijePronadjenException("Proizvod nije pronađen."));
		Korisnik vlasnik = korisnikRepository.findByKorisnickoIme(korisnickoIme)
				.orElseThrow(() -> new EntitetNijePronadjenException("Korisnik nije pronađen."));
		if (proizvod.getKupovina() != null)
			throw new PogresanZahtjevException("Proizvod vise nije aktivan.");
		if (proizvod.getVlasnik().getId() != vlasnik.getId())
			throw new PogresanZahtjevException("Dozvoljeno je obrisati jedino sopstveni proizvod.");
		proizvodRepository.delete(proizvod);
	}

	public void dodajProizvodSpecifikaciju(ProizvodSpecifikacijaZahtjev zahtjev) {
		if (!proizvodRepository.existsById(zahtjev.getProizvodId())) {
			throw new EntitetNijePronadjenException("Proizvod sa id-em " + zahtjev.getProizvodId() + " nije pronađen.");
		}
		if (!specifikacijaRepository.existsById(zahtjev.getSpecifikacijaId())) {
			throw new EntitetNijePronadjenException(
					"Specifikacija sa id-em " + zahtjev.getSpecifikacijaId() + " nije pronađena.");
		}
		ProizvodSpecifikacija proizvodSpecifikacija = modelMapper.map(zahtjev, ProizvodSpecifikacija.class);
		proizvodSpecifikacijaRepository.save(proizvodSpecifikacija);
	}

	public Map<String, Object> vratiKomentareProizvoda(Integer proizvodId,
			@RequestParam(defaultValue = "0") int trenutnaStranica,
			@RequestParam(defaultValue = "9") int brojElemenata) {

		Map<String, Object> odgovor = new HashMap<>();
		List<ProizvodKomentar> listaKomentara = new ArrayList<>();

		Pageable pagingSort = PageRequest.of(trenutnaStranica, brojElemenata);
		Page<ProizvodKomentar> pageKomentariProizvoda;

		Proizvod proizvod = proizvodRepository.findById(proizvodId)
				.orElseThrow(() -> new EntitetNijePronadjenException("Proizvod nije pronadjen."));

		pageKomentariProizvoda = proizvodKomentarRepository.findByProizvod(proizvod, pagingSort);
		listaKomentara = pageKomentariProizvoda.getContent();

		List<ProizvodKomentarOdgovor> komentari = listaKomentara.stream().map(this::mapirajKomentareUDTO)
				.collect(Collectors.toList());

		odgovor.put("komentari", komentari);
		odgovor.put("trenutnaStranica", pageKomentariProizvoda.getNumber());
		odgovor.put("ukupnoElemenata", pageKomentariProizvoda.getTotalElements());
		odgovor.put("brojStranica", pageKomentariProizvoda.getTotalPages());
		return odgovor;
	}

	public void dodajKomentar(ProizvodKomentarZahtjev zahtjev) {
		Proizvod proizvod = proizvodRepository.findById(zahtjev.getProizvodId())
				.orElseThrow(() -> new EntitetNijePronadjenException(
						"Proizvod sa id-em " + zahtjev.getProizvodId() + " nije pronađen."));
		Korisnik komentator = korisnikRepository.findByKorisnickoIme(zahtjev.getKorisnickoIme()).orElseThrow(
				() -> new EntitetNijePronadjenException("Korisnik " + zahtjev.getKorisnickoIme() + " nije pronađen."));
		ProizvodKomentar komentar = ProizvodKomentar.builder().komentar(zahtjev.getKomentar())
				.vrijeme(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString()).korisnik(komentator)
				.proizvod(proizvod).build();
		proizvodKomentarRepository.save(komentar);
	}

	public boolean obrisiKomentarProizvoda(Integer id) {
		Optional<ProizvodKomentar> optionalKomentar = proizvodKomentarRepository.findById(id);
		if (optionalKomentar.isPresent()) {
			ProizvodKomentar komentar = optionalKomentar.get();
			proizvodKomentarRepository.delete(komentar);
			return true;
		}
		return false;
	}

	private ProizvodKarticaOdgovor mapirajProizvodKarticeUDTO(Proizvod proizvod) {
		ProizvodKarticaOdgovor dto = modelMapper.map(proizvod, ProizvodKarticaOdgovor.class);
		Kategorija kategorija = kategorijaRepository.findById(proizvod.getKategorija().getId())
				.orElseThrow(() -> new EntitetNijePronadjenException(
						"Kategorija sa id-em " + proizvod.getKategorija().getId() + " nije pronađena."));
		dto.setNazivKategorije(kategorija.getNaziv());
		SlikaInfo slikaInfo = slikaService.vratiPoster(proizvod.getId()).map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(ProizvodController.class, "vratiSliku", path.getFileName().toString()).build()
					.toString();
			return new SlikaInfo(filename, url);
		}).findFirst().orElseThrow(() -> new RuntimeException("Poster nije pronadjen."));
		dto.setPosterUrl(slikaInfo.getUrl());
		return dto;
	}

	private List<String> ucitajSlikeProizvoda(Integer proizvodId) {
		List<SlikaInfo> fileInfos = slikaService.vratiSlikeProizvoda(proizvodId).map(path -> {
			String filename = path.getFileName().toString();
			String url = MvcUriComponentsBuilder
					.fromMethodName(ProizvodController.class, "vratiSliku", path.getFileName().toString()).build()
					.toString();
			return new SlikaInfo(filename, url);
		}).collect(Collectors.toList());
		List<String> slikeProizvodaUrl = fileInfos.stream().map(fi -> fi.getUrl()).collect(Collectors.toList());
		return slikeProizvodaUrl;
	}

	private ProizvodKomentarOdgovor mapirajKomentareUDTO(ProizvodKomentar komentar) {
		ProizvodKomentarOdgovor dto = modelMapper.map(komentar, ProizvodKomentarOdgovor.class);
		Korisnik korisnik = korisnikRepository.findById(komentar.getKorisnik().getId())
				.orElseThrow(() -> new EntitetNijePronadjenException("Korisnik nije pronađen"));
		dto.setAvatarUrl(korisnik.getAvatarUrl());
		dto.setKorisnickoIme(korisnik.getKorisnickoIme());
		return dto;
	}

}
