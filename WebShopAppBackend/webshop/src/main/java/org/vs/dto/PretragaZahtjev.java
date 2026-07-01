package org.vs.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PretragaZahtjev {

	private Integer kategorijaId;

	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9_]*$")
	@Size(min = 3, max = 100)
	private String korisnickoIme;

	@NotBlank
	@Size(min = 3, max = 30)
	private String kratakNaslov;

	@Size(min = 2, max = 50)
	private String lokacija;

	@Positive
	@Digits(integer = 10, fraction = 2)
	@NotNull
	private Double minCijena;

	@Positive
	@Digits(integer = 10, fraction = 2)
	@NotNull
	private Double maxCijena;

	@NotNull
	@PastOrPresent
	private String datumOd;

	@NotNull
	@PastOrPresent
	private String datumDo;

	private boolean polovan;

	private boolean nov;

}
