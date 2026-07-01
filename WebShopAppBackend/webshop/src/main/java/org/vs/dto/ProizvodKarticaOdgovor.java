package org.vs.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProizvodKarticaOdgovor {

	@NotBlank
	private Integer id;

	@NotBlank
	@Size(min = 3, max = 30)
	private String kratakNaslov;

	@Positive
	@Digits(integer = 10, fraction = 2)
	@NotNull
	private Double cijena;

	@NotBlank
	private String nazivKategorije;

	@NotBlank
	private String posterUrl;

}
