package dto;

import java.io.Serializable;
import java.util.Objects;

public class Korisnik implements Serializable {

	private static final long serialVersionUID = -4711498943577014210L;

	private Integer id;

	private String ime;

	private String prezime;

	private String grad;

	private String email;

	private String brojTelefona;

	private String korisnickoIme;

	private String lozinka;

	private String avatarUrl;

	private String datumPristupa;

	private String pin;

	private String uloga;

	public Korisnik() {
		super();
	}

	public Korisnik(String ime, String prezime, String grad, String email, String brojTelefona, String korisnickoIme,
			String lozinka, String avatarUrl, String datumPristupa, String pin, String uloga) {
		super();
		this.ime = ime;
		this.prezime = prezime;
		this.grad = grad;
		this.email = email;
		this.brojTelefona = brojTelefona;
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.avatarUrl = avatarUrl;
		this.datumPristupa = datumPristupa;
		this.pin = pin;
		this.uloga = uloga;
	}

	public Korisnik(Integer id, String ime, String prezime, String grad, String email, String brojTelefona,
			String korisnickoIme, String lozinka, String avatarUrl, String datumPristupa, String pin, String uloga) {
		super();
		this.id = id;
		this.ime = ime;
		this.prezime = prezime;
		this.grad = grad;
		this.email = email;
		this.brojTelefona = brojTelefona;
		this.korisnickoIme = korisnickoIme;
		this.lozinka = lozinka;
		this.avatarUrl = avatarUrl;
		this.datumPristupa = datumPristupa;
		this.pin = pin;
		this.uloga = uloga;
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

	public String getGrad() {
		return grad;
	}

	public void setGrad(String grad) {
		this.grad = grad;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBrojTelefona() {
		return brojTelefona;
	}

	public void setBrojTelefona(String brojTelefona) {
		this.brojTelefona = brojTelefona;
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

	public String getAvatarUrl() {
		return avatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}

	public String getDatumPristupa() {
		return datumPristupa;
	}

	public void setDatumPristupa(String datumPristupa) {
		this.datumPristupa = datumPristupa;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getUloga() {
		return uloga;
	}

	public void setUloga(String uloga) {
		this.uloga = uloga;
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
		Korisnik other = (Korisnik) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "Korisnik [id=" + id + ", ime=" + ime + ", prezime=" + prezime + ", grad=" + grad + ", email=" + email
				+ ", brojTelefona=" + brojTelefona + ", korisnickoIme=" + korisnickoIme + ", lozinka=" + lozinka
				+ ", avatarUrl=" + avatarUrl + ", datumPristupa=" + datumPristupa + ", pin=" + pin + ", uloga=" + uloga
				+ "]";
	}

}