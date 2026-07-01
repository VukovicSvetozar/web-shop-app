package org.vs.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PorukaKorisnickePodrskeZahtjev {

	@NotBlank
	@Size(min = 3, max = 30)
	private String naslovPoruke;

	@NotBlank
	@Size(min = 3, max = 300)
	private String tekstPoruke;

	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9_]*$")
	@Size(min = 3, max = 100)
	private String korisnickoIme;

}
