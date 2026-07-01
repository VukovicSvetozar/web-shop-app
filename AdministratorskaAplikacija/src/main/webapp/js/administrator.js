
// Dodavanje nove kategorije.
$(document).ready(function() {
	$('#kategorija-dodaj-button-sacuvaj').on('click', function() {
		var nazivKategorije = document.getElementById("kategorija-naziv").value;
		$.ajax({
			type: 'GET',
			url: 'Administrator?akcija=dodajKategoriju',
			data: { nazivKategorije: nazivKategorije },
			success: function(data) {
				if (data.dodato) {
					$('#kategorija-naziv').val('');
					location.search = location.search.replace('akcija=prijava', 'akcija=kategorije');
					var modal = document.getElementById("kategorija-modal-dodaj");
					var bootstrapModal = bootstrap.Modal.getInstance(modal);
					bootstrapModal.hide();
				} else {
					alert("Uneseni naziv kategorije vec postoji.");
					$('#kategorija-naziv').val('');
					$('.kategorija-naziv-dostupan').empty();
				}
			}
		});
	});
});

// Provjera da li je unesen postojeci naziv za kategoriju pri dodavanju kategorije.
$(document).ready(function() {
	$('#kategorija-naziv').on('keyup', function() {
		var nazivKategorije = document.getElementById("kategorija-naziv").value;
		console.log("naziv kategorije", nazivKategorije);
		$.ajax({
			type: 'GET',
			url: 'Administrator?akcija=provjeraNazivaKategorije',
			data: { nazivKategorije: nazivKategorije },
			success: function(data) {
				console.log("test");
				if (data.dostupno) {
					$('.kategorija-naziv-dostupan').text('Naziv kategorije je dostupan.');
				} else {
					$('.kategorija-naziv-dostupan').text('Naziv kategorije je zauzet.');
				}
			}
		});
	});
});

// Odustajanje od dodavanja nove kategorije.
$(document).ready(function() {
	$('#kategorija-dodaj-button-zatvori').on('click', function() {
		$('#kategorija-naziv').val('');
		$('.kategorija-naziv-dostupan').empty();

	});
});

// Odustajanje od dodavanja nove kategorije.
$(document).ready(function() {
	$('#kategorija-dodaj-button-zatvori-x').on('click', function() {
		$('#kategorija-naziv').val('');
		$('.kategorija-naziv-dostupan').empty();

	});
});

// Uredjivanje kategorije.
$(document).ready(function() {
	$('.kategorija-tabela-kolona-uredi').click(function() {
		var odabraniRed = $(this).closest('tr');
		var idKategorija = odabraniRed.data('id');
		var originalNaziv = odabraniRed.data('naziv');
		$('#kategorija-naziv-uredi').val(originalNaziv);

		$('#kategorija-naziv-uredi').on('keyup', function() {
			var noviNaziv = $(this).val();
			if (noviNaziv === originalNaziv) {
				$('.kategorija-naziv-dostupan').text('Naziv je nepromjenjen.');
			} else {
				$.ajax({
					type: 'GET',
					url: 'Administrator?akcija=provjeraNazivaKategorije',
					data: { nazivKategorije: noviNaziv },
					success: function(data) {
						if (data.dostupno) {
							$('.kategorija-naziv-dostupan').text('Naziv je dostupan.');
						} else {
							$('.kategorija-naziv-dostupan').text('Naziv je zauzet.');
						}
					}
				});
			}
		});

		$('#kategorija-uredi-button-sacuvaj').on('click', function(event) {
			event.preventDefault();
			var form = document.getElementById("kategorija-uredi-form");
			var inputs = form.querySelectorAll('input');
			var isValidForm = true;
			inputs.forEach(function(input) {
				var errorMessage = $(input).next('.invalid-feedback');
				if (!input.checkValidity()) {
					errorMessage.show();
					$(input).addClass('is-invalid');
					isValidForm = false;
				} else {
					errorMessage.hide();
					$(input).removeClass('is-invalid');
				}
			});

			if (isValidForm) {
				var nazivKategorije = document.getElementById("kategorija-naziv-uredi").value;
				$.ajax({
					type: 'GET',
					url: 'Administrator?akcija=urediKategoriju',
					data: {
						idKategorija: idKategorija, nazivKategorije: nazivKategorije
					},
					success: function(data) {
						if (data.promjenjeno) {
							$('#kategorija-naziv-uredi').val('');
							$(form).find('.invalid-feedback').hide();
							location.search = location.search.replace('akcija=prijava', 'akcija=kategorije');
							var modal = document.getElementById("kategorija-modal-uredi");
							var bootstrapModal = bootstrap.Modal.getInstance(modal);
							bootstrapModal.hide();
						} else {
							alert("Nije dozvoljeno azuriranje kategorije navedenom vrijednoscu.");
							$('korisnik-ime-uredi').val('');
						}
					}
				});
			}
		});

		$('#kategorija-uredi-button-zatvori').on('click', function() {
			$('#kategorija-naziv-uredi').val('');
			$('.kategorija-naziv-dostupan').empty();
			$('#kategorija-naziv-uredi').off('keyup');
		});
	});
});

// Brisanje kategorije.
$(document).ready(function() {
	$('.kategorija-tabela-kolona-obrisi').on('click', function() {
		if (confirm("Da li ste sigurni da želite obrisati odabranu kategoriju?")) {
			var red = $(this).closest('tr');
			var idKategorije = red.find('td:eq(0)').text();
			$.ajax({
				type: 'GET',
				url: 'Administrator?akcija=obrisiKategoriju',
				data: { idKategorije: idKategorije },
				success: function(data) {
					if (data.obrisano) {
						$('#kategorija-alert-brisanje-uspjesno').fadeIn();
						setTimeout(function() {
							$('#kategorija-alert-brisanje-uspjesno').fadeOut();
							location.search = location.search.replace('akcija=prijava', 'akcija=kategorije');
						}, 2000);
					} else {
						$('#kategorija-alert-brisanje-neuspjesno').fadeIn();
						setTimeout(function() {
							$('#kategorija-alert-brisanje-neuspjesno').fadeOut();
							location.search = location.search.replace('akcija=prijava', 'akcija=kategorije');
						}, 2000);
					}
				}
			});
		} else {
			return;
		}
	});
});

// Prikaz specifikacija.
$(document).ready(function() {
	$('.specifikacija-tabela-kolona').on('click', function() {
		var odabraniRed = $(this).closest('tr');
		var idKategorija = odabraniRed.data('id');
		$.ajax({
			type: 'GET',
			url: 'Administrator?akcija=specifikacije',
			data: { idKategorija: idKategorija },
			success: function(data) {
				var listaSpecifikacija = document.getElementById("specifikacija-lista");
				listaSpecifikacija.innerHTML = "";
				data.forEach(function(specifikacija) {
					var liElement = document.createElement("li");
					liElement.textContent = specifikacija.naziv;
					liElement.dataset.id = specifikacija.id;
					liElement.dataset.naziv = specifikacija.naziv;
					liElement.dataset.idKategorija = specifikacija.idKategorija;
					listaSpecifikacija.appendChild(liElement);

					var $span = $("<span></span>").addClass("specifikacija-zatvori").html("&times;");
					$(liElement).append($span);
				});

				$(".specifikacija-zatvori").click(function() {
					if (confirm("Da li sigurni da želite obrisati odabranu specifikaciju?")) {
						var idSpecifikacija = $(this).parent().data("id");
						$(this).parent().hide();
						$.ajax({
							type: 'GET',
							url: 'Administrator?akcija=obrisiSpecifikaciju',
							data: { idSpecifikacija: idSpecifikacija },
							success: function(odgovor) {
								if (odgovor.obrisanaSpecifikacija) {
									$('#specifikacija-alert-brisanje-uspjesno').fadeIn();
									setTimeout(function() {
										$('#specifikacija-alert-brisanje-uspjesno').fadeOut();
									}, 2000);
								}
							}
						});
					}
				});

				$(".specifikacija-button-dodaj").click(function() {
					console.log("333333", idKategorija);
					var unesenaSpecifikacija = $("#specifikacija-naziv").val();
					if (unesenaSpecifikacija === '') {
						console.log("444444", idKategorija);
						alert("Morate unijeti naziv specifikacije!");
					} else {
						$.ajax({
							type: 'GET',
							url: 'Administrator?akcija=dodajSpecifikaciju',
							data: { nazivSpecifikacije: unesenaSpecifikacija, idKategorija: idKategorija },
							success: function(odgovor) {
								console.log("5555555", idKategorija);
								if (odgovor.dodato) {
									var liElement = $("<li></li>").text(unesenaSpecifikacija);
									$("#specifikacija-lista").append(liElement);
									$("#specifikacija-naziv").val("");
									var span = $("<span></span>").addClass("specifikacija-zatvori").text("\u00D7");
									liElement.append(span);
									$(".specifikacija-zatvori").click(function() {
										$(this).parent().hide();
									});
									$('#specifikacija-alert-dodavanje-uspjesno').fadeIn();
									setTimeout(function() {
										$('#specifikacija-alert-dodavanje-uspjesno').fadeOut();
									}, 2000);
								} else {
									$('#specifikacija-alert-dodavanje-neuspjesno').fadeIn();
									setTimeout(function() {
										$('#specifikacija-alert-dodavanje-neuspjesno').fadeOut();
									}, 2000);
								}
							}
						});
					}
				});
			}
		});
	});
});

// Resetovanje modala specifikacije.
$(document).ready(function() {
	$("#specifikacija-button-ok").click(function() {
		$("#specifikacija-lista").empty();
		$("#specifikacija-naziv").val("");
		$('#specifikacija-modal').modal('hide');
		$(".specifikacija-button-dodaj").off("click");
	});
});

// Provjera uskladjenosti datuma kod statistike.
$(document).ready(function() {
	$('#datumOd, #datumDo').change(function() {
		var datumOd = document.getElementById("datumOd").value;
		var datumDo = document.getElementById("datumDo").value;
		if (datumOd.length != 0 && datumDo != 0) {
			var ranije = new Date(datumOd);
			var novije = new Date(datumDo);
			if (ranije < novije) {
				var danger = document.getElementById("alert-datum-danger");
				danger.style.display = "none";
				var success = document.getElementById("alert-datum-success");
				success.style.display = "block";
			} else {
				var danger = document.getElementById("alert-datum-danger");
				danger.style.display = "block";
				var success = document.getElementById("alert-datum-success");
				success.style.display = "none";
			}
		}
		else {
			var danger = document.getElementById("alert-datum-danger");
			danger.style.display = "none";
			var success = document.getElementById("alert-datum-success");
			success.style.display = "none";
		}
	});
});

// Prihvatanje log poruka.
$(document).ready(function() {
	$('#trazi-statistiku-button').on('click', function() {
		console.log("test");
		var parametri = {};
		var startTime = document.getElementById("datumOd").value;
		var endTime = document.getElementById("datumDo").value;
		if (startTime.length != 0 && endTime != 0) {
			var ranije = new Date(startTime);
			var novije = new Date(endTime);
			if (ranije < novije) {
				parametri.startTime = startTime;
				parametri.endTime = endTime;
			} else {
				alert("Vremena nisu uskladjena.");
				return;
			}
		} else {
			alert("Vremena nema.");
			return;
		}
		var logLevelInfo = document.getElementById("switchCheckInfo");
		if (logLevelInfo != null && logLevelInfo.checked) {
			parametri.logLevelInfo = 'info';
		}
		var logLevelTrace = document.getElementById("switchCheckTrace");
		if (logLevelTrace != null && logLevelTrace.checked) {
			parametri.logLevelTrace = 'trace';
		}
		var logLevelDebug = document.getElementById("switchCheckDebug");
		if (logLevelDebug != null && logLevelDebug.checked) {
			parametri.logLevelDebug = 'debug';
		}
		var logLevelWarn = document.getElementById("switchCheckWarn");
		if (logLevelWarn != null && logLevelWarn.checked) {
			parametri.logLevelWarn = 'warn';
		}
		var logLevelError = document.getElementById("switchCheckError");
		if (logLevelError != null && logLevelError.checked) {
			parametri.logLevelError = 'error';
		}
		var logLevelFatal = document.getElementById("switchCheckFatal");
		if (logLevelFatal != null && logLevelFatal.checked) {
			parametri.logLevelFatal = 'fatal';
		}

		var queryString = Object.keys(parametri).map(function(kljuc) {
			return encodeURIComponent(kljuc) + "=" + encodeURIComponent(parametri[kljuc]);
		}).join("&");
		var url = 'http://localhost:8888/api/logovi' + (queryString ? "?" + queryString : "");

		$.ajax({
			type: 'GET',
			url: url,
			data: {},
			success: function(data) {
				var listaLogova = document.getElementById("listaLogova");
				console.log(data);
				listaLogova.innerHTML = "";
				var stavke = data.split("\n");
				stavke.forEach(function(stavka) {
					var elementiStavke = stavka.split("#");
					var logLevel = elementiStavke[1].trim().toLowerCase();
					var li = document.createElement("li");
					li.textContent = stavka;
					li.classList.add("list-group-item");
					if (logLevel === "info") {
						li.classList.add("list-group-item-success");
					} else if (logLevel === "trace") {
						li.classList.add("list-group-item-info");
					} else if (logLevel === "degug") {
						li.classList.add("list-group-item-secondary");
					} else if (logLevel === "warn") {
						li.classList.add("list-group-item-warning");
					} else if (logLevel === "error") {
						li.classList.add("list-group-item-danger");
					} else if (logLevel === "fatal") {
						li.classList.add("list-group-item-dark");
					}
					listaLogova.appendChild(li);
				});
			}
		});
	});
});

// Dodavanje novog korisnika.
$(document).ready(function() {
	$('#korisnik-dodaj-button-sacuvaj').on('click', function(event) {
		event.preventDefault();
		var form = document.getElementById("korisnik-dodaj-form");
		var inputs = form.querySelectorAll('input');
		var isValidForm = true;
		inputs.forEach(function(input) {
			var errorMessage = $(input).next('.invalid-feedback');
			if (!input.checkValidity()) {
				errorMessage.show();
				$(input).addClass('is-invalid');
				isValidForm = false;
			} else {
				errorMessage.hide();
				$(input).removeClass('is-invalid');
			}
		});
		if (isValidForm) {
			var imeKorisnika = document.getElementById("korisnik-ime").value;
			var prezimeKorisnika = document.getElementById("korisnik-prezime").value;
			var gradKorisnika = document.getElementById("korisnik-grad").value;
			var emailKorisnika = document.getElementById("korisnik-email").value;
			var brojTelefonaKorisnika = document.getElementById("korisnik-brojTelefona").value;
			var korisnickoIme = document.getElementById("korisnik-korisnickoIme").value;
			var lozinkaKorisnika = document.getElementById("korisnik-lozinka").value;
			$.ajax({
				type: 'GET',
				url: 'Administrator?akcija=dodajKorisnika',
				data: {
					imeKorisnika: imeKorisnika, prezimeKorisnika: prezimeKorisnika, gradKorisnika: gradKorisnika,
					emailKorisnika: emailKorisnika, brojTelefonaKorisnika: brojTelefonaKorisnika,
					korisnickoIme: korisnickoIme, lozinkaKorisnika: lozinkaKorisnika
				},
				success: function(data) {
					if (data.dodato) {
						$('korisnik-ime').val('');
						$('korisnik-prezime').val('');
						$('korisnik-grad').val('');
						$('korisnik-email').val('');
						$('korisnik-brojTelefona').val('');
						$('korisnik-korisnickoIme').val('');
						$('korisnik-lozinka').val('');

						$(form).find('.invalid-feedback').hide();
						location.search = location.search.replace('akcija=prijava', 'akcija=korisnici');
						var modal = document.getElementById("korisnik-modal-dodaj");
						var bootstrapModal = bootstrap.Modal.getInstance(modal);
						bootstrapModal.hide();
					} else {
						alert("Doslo je do greske pri dodavanju korisnika.");
						$('korisnik-ime').val('');
						$('korisnik-prezime').val('');
						$('korisnik-grad').val('');
						$('korisnik-email').val('');
						$('korisnik-brojTelefona').val('');
						$('korisnik-korisnickoIme').val('');
						$('korisnik-lozinka').val('');
					}
				}
			});
		}
	});
});

// Odustajanje od dodavanja novog korisnika.
$(document).ready(function() {
	$('#korisnik-dodaj-button-zatvori').on('click', function() {
		var form = $('#korisnik-dodaj-form');
		form.find('input').val('');
		form.find('.invalid-feedback').hide();
		form.find('input').removeClass('is-invalid');
	});
});

// Provjera da li je uneseno postojece korisnicko ime pri dodavanju korisnika.
$(document).ready(function() {
	$('#korisnik-korisnickoIme').on('keyup', function() {
		var korisnickoIme = document.getElementById("korisnik-korisnickoIme").value;
		$.ajax({
			type: 'GET',
			url: 'Administrator?akcija=provjeraKorisnickogImena',
			data: { korisnickoIme: korisnickoIme },
			success: function(data) {
				if (data.dostupno) {
					$('.korisnik-korisnickoIme-dostupan').text('Korisnicko ime je dostupno.');
				} else {
					$('.korisnik-korisnickoIme-dostupan').text('Korisnicko ime je zauzeto.');
				}
			}
		});
	});
});

// Provjera da li je unesen postojeci email pri dodavanju korisnika.
$(document).ready(function() {
	$('#korisnik-email').on('keyup', function() {
		var email = document.getElementById("korisnik-email").value;
		$.ajax({
			type: 'GET',
			url: 'Administrator?akcija=provjeraEmaila',
			data: { email: email },
			success: function(data) {
				if (data.dostupno) {
					$('.korisnik-email-dostupan').text('Email je dostupan.');
				} else {
					$('.korisnik-email-dostupan').text('Email je zauzet.');
				}
			}
		});
	});
});

// Profil korisnika
$(document).ready(function() {
	$('.korisnik-tabela-kolona-info').click(function() {
		var idKorisnik = $(this).closest('tr').find('td:nth-child(1)').text();
		$.ajax({
			type: 'GET',
			url: 'Administrator?akcija=prikaziKorisnika',
			data: { idKorisnik: idKorisnik },
			success: function(data) {
				$('#korisnik-ime-prikaz').text(data.ime);
				$('#korisnik-prezime-prikaz').text(data.prezime);
				$('#korisnik-grad-prikaz').text(data.grad);
				$('#korisnik-email-prikaz').text(data.email);
				$('#korisnik-brojTelefona-prikaz').text(data.brojTelefona);
				$('#korisnik-korisnickoIme-prikaz').text(data.korisnickoIme);
				$('#korisnik-datumPristupa-prikaz').text(data.datumPristupa);
				$('#korisnik-status-prikaz').text(data.uloga);
				$('#korisnik-modal-info').modal('show');

			},
			error: function(error) {
				console.error("Greška tokom prikaza korisnika: ", error);
			}
		});
	});
});

// Uredjivanje korisnika.
$(document).ready(function() {
	$('.korisnik-tabela-kolona-uredi').click(function() {
		var odabraniRed = $(this).closest('tr');
		var idKorisnik = odabraniRed.data('id');
		var originalKorisnickoIme = odabraniRed.data('korisnickoime');
		var originalEmail = odabraniRed.data('email');

		$.ajax({
			type: 'GET',
			url: 'Administrator?akcija=prikaziKorisnika',
			data: { idKorisnik: idKorisnik },
			success: function(data) {
				$('#korisnik-ime-uredi').val(data.ime);
				$('#korisnik-prezime-uredi').val(data.prezime);
				$('#korisnik-grad-uredi').val(data.grad);
				$('#korisnik-email-uredi').val(data.email);
				$('#korisnik-brojTelefona-uredi').val(data.brojTelefona);
				$('#korisnik-korisnickoIme-uredi').val(data.korisnickoIme);
			},
			error: function(error) {
				console.error("Greška tokom prikaza korisnika: ", error);
			}
		});

		$('#korisnik-korisnickoIme-uredi').on('keyup', function() {
			var noviKorisnickoIme = $(this).val();
			if (noviKorisnickoIme === originalKorisnickoIme) {
				$('.korisnik-korisnickoIme-dostupan').text('Korisnicko ime je nepromjenjeno.');
			} else {
				$.ajax({
					type: 'GET',
					url: 'Administrator?akcija=provjeraKorisnickogImena',
					data: { korisnickoIme: noviKorisnickoIme },
					success: function(data) {
						if (data.dostupno) {
							$('.korisnik-korisnickoIme-dostupan').text('Korisnicko ime je dostupno.');
						} else {
							$('.korisnik-korisnickoIme-dostupan').text('Korisnicko ime je zauzeto.');
						}
					}
				});
			}
		});

		// Provjera dostupnosti emaila dok korisnik unosi
		$('#korisnik-email-uredi').on('keyup', function() {
			var noviEmail = $(this).val();

			if (noviEmail === originalEmail) {
				$('.korisnik-email-dostupan').text('Email je nepromjenjen');
			} else {
				$.ajax({
					type: 'GET',
					url: 'Administrator?akcija=provjeraEmaila',
					data: { email: noviEmail },
					success: function(data) {
						if (data.dostupno) {
							$('.korisnik-email-dostupan').text('Email je dostupan.');
						} else {
							$('.korisnik-email-dostupan').text('Email je zauzet.');
						}
					}
				});
			}
		});

		$('#korisnik-uredi-button-sacuvaj').on('click', function(event) {
			event.preventDefault();
			var form = document.getElementById("korisnik-uredi-form");
			var inputs = form.querySelectorAll('input');
			var isValidForm = true;
			inputs.forEach(function(input) {
				var errorMessage = $(input).next('.invalid-feedback');
				if (!input.checkValidity()) {
					errorMessage.show();
					$(input).addClass('is-invalid');
					isValidForm = false;
				} else {
					errorMessage.hide();
					$(input).removeClass('is-invalid');
				}
			});

			if (isValidForm) {
				var imeKorisnika = document.getElementById("korisnik-ime-uredi").value;
				var prezimeKorisnika = document.getElementById("korisnik-prezime-uredi").value;
				var gradKorisnika = document.getElementById("korisnik-grad-uredi").value;
				var emailKorisnika = document.getElementById("korisnik-email-uredi").value;
				var brojTelefonaKorisnika = document.getElementById("korisnik-brojTelefona-uredi").value;
				var korisnickoIme = document.getElementById("korisnik-korisnickoIme-uredi").value;
				var lozinkaKorisnika = document.getElementById("korisnik-lozinka-uredi").value;
				$.ajax({
					type: 'GET',
					url: 'Administrator?akcija=urediKorisnika',
					data: {
						idKorisnik: idKorisnik, imeKorisnika: imeKorisnika, prezimeKorisnika: prezimeKorisnika, gradKorisnika: gradKorisnika,
						emailKorisnika: emailKorisnika, brojTelefonaKorisnika: brojTelefonaKorisnika,
						korisnickoIme: korisnickoIme, lozinkaKorisnika: lozinkaKorisnika
					},
					success: function(data) {
						if (data.promjenjeno) {
							$('korisnik-ime-uredi').val('');
							$('korisnik-prezime-uredi').val('');
							$('korisnik-grad-uredi').val('');
							$('korisnik-email-uredi').val('');
							$('korisnik-brojTelefona-uredi').val('');
							$('korisnik-korisnickoIme-uredi').val('');
							$('korisnik-lozinka-uredi').val('');

							$(form).find('.invalid-feedback').hide();
							location.search = location.search.replace('akcija=prijava', 'akcija=korisnici');
							var modal = document.getElementById("korisnik-modal-uredi");
							var bootstrapModal = bootstrap.Modal.getInstance(modal);
							bootstrapModal.hide();
						} else {
							alert("Doslo je do greske pri azuriranju korisnika.");
							$('korisnik-ime-uredi').val('');
							$('korisnik-prezime-uredi').val('');
							$('korisnik-grad-uredi').val('');
							$('korisnik-email-uredi').val('');
							$('korisnik-brojTelefona-uredi').val('');
							$('korisnik-korisnickoIme-uredi').val('');
							$('korisnik-lozinka-uredi').val('');
						}
					}
				});
			}
		});

		$('#korisnik-uredi-button-zatvori').on('click', function() {
			$('#korisnik-ime-uredi').val('');
			$('#korisnik-prezime-uredi').val('');
			$('#korisnik-grad-uredi').val('');
			$('#korisnik-email-uredi').val('');
			$('#korisnik-brojTelefona-uredi').val('');
			$('#korisnik-korisnickoIme-uredi').val('');
			$('#korisnik-lozinka-uredi').val('');

			$('.korisnik-korisnickoIme-dostupan').empty();
			$('.korisnik-email-dostupan').empty();
			$('#korisnik-korisnickoIme-uredi').off('keyup');
			$('#korisnik-email-uredi').off('keyup');
		});
	});
});

// Brisanje korisnika.
$(document).ready(function() {
	$('.korisnik-tabela-kolona-obrisi').on('click', function() {
		if (confirm("Da li ste sigurni da želite obrisati odabranog korisnika?")) {
			var red = $(this).closest('tr');
			var idKorisnik = red.find('td:eq(0)').text();
			$.ajax({
				type: 'GET',
				url: 'Administrator?akcija=obrisiKorisnika',
				data: { idKorisnik: idKorisnik },
				success: function(data) {
					if (data.obrisano) {
						$('#korisnik-alert-brisanje-uspjesno').fadeIn();
						setTimeout(function() {
							$('#korisnik-alert-brisanje-uspjesno').fadeOut();
							location.search = location.search.replace('akcija=prijava', 'akcija=korisnici');
						}, 2000);
					} else {
						$('#korisnik-alert-brisanje-neuspjesno').fadeIn();
						setTimeout(function() {
							$('#korisnik-alert-brisanje-neuspjesno').fadeOut();
							location.search = location.search.replace('akcija=prijava', 'akcija=korisnici');
						}, 2000);
					}
				}
			});
		} else {
			return;
		}
	});
});
