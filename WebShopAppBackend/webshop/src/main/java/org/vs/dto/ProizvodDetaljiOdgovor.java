package org.vs.dto;

import java.util.*;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProizvodDetaljiOdgovor {

	private String avatarUrl;

	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9_]*$")
	@Size(min = 3, max = 100)
	private String korisnickoIme;

	@NotNull
	@PastOrPresent
	private String datumPristupa;

	@NotBlank
	@Size(max = 30)
	private String grad;

	@NotBlank
	@Email
	@Size(max = 100)
	private String email;

	@Pattern(regexp = "^[- +()0-9]+$")
	private String brojTelefona;

	@NotNull
	@PastOrPresent
	private String vrijemeObjave;

	@NotBlank
	@Size(min = 3, max = 30)
	private String kratakNaslov;

	@NotBlank
	@Size(min = 3, max = 300)
	private String detaljanOpis;

	@Positive
	@Digits(integer = 10, fraction = 2)
	@NotNull
	private Double cijena;

	private boolean polovan;

	@Size(min = 2, max = 50)
	private String lokacija;

	@NotBlank
	private String nazivKategorije;

	private Map<String, String> specificneOsobine;
	
	private List<String> slikeProizvodaUrl;

}
