package org.vs.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProizvodStatistikaOdgovor {

	@NotBlank
	@PositiveOrZero
	private Integer brojAktivnih;

	@NotBlank
	@PositiveOrZero
	private Integer brojZavrsenih;

	@NotBlank
	@PositiveOrZero
	private Integer brojKupljenih;

}
