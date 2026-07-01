package beans;

import java.io.Serializable;
import java.util.Objects;

public class NalogPodrska implements Serializable {

	private static final long serialVersionUID = 4382589202731829326L;

	private Integer id;

	private String ime;
	private String prezime;
	private String korisnickoIme;
	private String lozinka;
	private Integer uloga;

	private boolean prijavljen = false;

	public NalogPodrska(Integer id, String ime, String prezime, String korisnickoIme, String lozinka, Integer uloga) {
		super();
		this.id = id;
		this.ime = ime;
		this.prezime = prezime;
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.uloga = uloga;
	}

	public NalogPodrska() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public String getKorisnickoIme() {
		return korisnickoIme;
	}

	public void setKorisnickoIme(String korisnickoIme) {
		this.korisnickoIme = korisnickoIme;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public Integer getUloga() {
		return uloga;
	}

	public void setUloga(Integer uloga) {
		this.uloga = uloga;
	}

	public boolean isPrijavljen() {
		return prijavljen;
	}

	public void setPrijavljen(boolean prijavljen) {
		this.prijavljen = prijavljen;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NalogPodrska other = (NalogPodrska) obj;
		return Objects.equals(id, other.id);
	}

}
