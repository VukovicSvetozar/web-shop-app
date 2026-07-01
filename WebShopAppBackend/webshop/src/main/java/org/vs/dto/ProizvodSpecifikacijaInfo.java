package org.vs.dto;

import lombok.*;

import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProizvodSpecifikacijaInfo {

	@Size(min = 3, max = 50)
	private String vrijednost;

	@NotBlank
	private String naziv;

}
