package org.vs.dto;

import lombok.*;

import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProizvodSpecifikacijaZahtjev {

	@Size(min = 3, max = 50)
	private String vrijednost;

	@NotNull
	private Integer proizvodId;

	@NotNull
	private Integer specifikacijaId;

}
