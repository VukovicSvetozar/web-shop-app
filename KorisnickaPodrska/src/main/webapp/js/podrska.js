
// Odjava iz aplikacije.
$(document).ready(function() {
	$('#button-odjava').on('click', function() {
		window.location.href = 'logout.jsp';
		localStorage.removeItem('jwtToken');
	});
});

// Rad sa porukama korisnicke podrske.
$(document).ready(function() {
	var jwtToken = localStorage.getItem('jwtToken');
	var url = 'http://localhost:8888/api/porukePodrske';
	$.ajax({
		type: 'GET',
		url: url,
		headers: {
			'Authorization': 'Bearer ' + jwtToken
		},
		data: {},
		success: function(data) {
			$('#table-poruke tbody').empty();
			// Dodaj nove redove sa podacima.
			for (var i = 0; i < data.length; i++) {
				var procitanaClass = data[i].procitana ? 'table-success' : 'table-danger';
				var red = '<tr class="' + procitanaClass + '">' +
					'<td>' + data[i].id + '</td>' +
					'<td>' + data[i].korisnickoIme + '</td>' +
					'<td>' + data[i].vrijemePoruke + '</td>' +
					'<td>' + data[i].naslovPoruke + '</td>' +
					'<td>' +
					'<button class="button-poruka-tekst btn btn-primary" data-tekst="' + data[i].tekstPoruke + '" data-poruka-id="' + data[i].id + '">Procitaj</button>' +
					'<td>' +
					'<button class="button-poruka-posalji btn btn-primary" data-email="' + data[i].email + '">Posalji</button>' +

					'</td>' +
					'</tr>';
				$('#table-poruke tbody').append(red);
			}

			// Prikaz modala za citanje poruka.
			$('.button-poruka-tekst').on('click', function() {
				var poruka = $(this).data('tekst');
				var porukaId = $(this).data('poruka-id');
				$('#modal-poruka-citanje .modal-body').text(poruka);
				$('#modal-poruka-citanje').data('poruka-id', porukaId);
				$('#modal-poruka-citanje').modal('show');
			});

			// Oznaci poruku kao procitanu
			$('#modal-poruka-citanje').on('shown.bs.modal', function() {
				var porukaId = $('#modal-poruka-citanje').data('poruka-id');
				var procitanaUrl = 'http://localhost:8888/api/porukePodrske/' + porukaId + '/procitana';
				$.ajax({
					type: 'PUT',
					url: procitanaUrl,
					headers: {
						'Authorization': 'Bearer ' + jwtToken
					},
					data: {},
					success: function() {
					}
				});
			});

			// Osvjezi stranicu poslije zatvaranja modala za citanje poruke.
			$('#modal-poruka-citanje').on('hidden.bs.modal', function() {
				location.reload();
			});

			// Prikaz modala za slanje odgovora.
			$('.button-poruka-posalji').on('click', function() {
				var mojEmail = $(this).data('email');
				$('#input-email').val(mojEmail).prop('readonly', true);
				$('#modal-poruka-odgovor').modal('show');
				var porukaId = $(this).closest('tr').find('td:first-child').text();
				$('#button-odgovor-posalji').data('poruka-id', porukaId);
			});

			// Odgovaranje putem email-a.
			$('#button-odgovor-posalji').on('click', function() {
				var naslovOdgovora = $('#input-naslov').val();
				var tekstOdgovora = $('#textarea-email').val();
				var emailPrimaoca = $('#input-email').val();
				var porukaId = $(this).data('poruka-id');
				var odgovorUrl = 'http://localhost:8888/api/porukePodrske/' + porukaId + '/odgovor';
				$.ajax({
					type: 'PUT',
					url: odgovorUrl,
					headers: {
						'Accept': 'application/json',
						'Content-Type': 'application/json',
						'Authorization': 'Bearer ' + jwtToken
					},
					data: JSON.stringify({
						naslov: naslovOdgovora,
						tekst: tekstOdgovora,
						email: emailPrimaoca
					}),
					success: function(odgovor) {
						if (odgovor) {
							console.log('Poruka je poslata. Ubaci neko obavjestenje.');
							var modal = document.getElementById("modal-poruka-odgovor");
							var bootstrapModal = bootstrap.Modal.getInstance(modal);
							bootstrapModal.hide();
						} else
							console.log('Poruka nije poslata. Ubaci neko obavjestenje.');
					}
				});
			});

			// Osvjezi stranicu poslije zatvaranja modala za citanje poruke.
			$('#modal-poruka-odgovor').on('hidden.bs.modal', function() {
				$('#input-naslov').val("");
				$('#textarea-email').val("");
			});

			// Realizovanje pretrage na osnovu teksta poruke.
			$('#input-pretraga').on('keyup', function() {
				var vrijednostPretrage = $(this).val().toLowerCase();
				$('#table-poruke').find('tbody tr').filter(function() {
					var celijaTekstPoruke = $(this).find('td:nth-child(5) .button-poruka-tekst').data('tekst');
					$(this).toggle(celijaTekstPoruke.toLowerCase().indexOf(vrijednostPretrage) > -1);
				});
			});
		}
	});
});
