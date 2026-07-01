package org.vs.dto;

import lombok.*;

import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProizvodZahtjev {

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

	@NotNull
	private Integer kategorijaId;

	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9_]*$")
	@Size(min = 3, max = 100)
	private String korisnickoIme;

}
