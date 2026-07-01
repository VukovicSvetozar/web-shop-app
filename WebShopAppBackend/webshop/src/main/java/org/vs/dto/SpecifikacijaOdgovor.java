package org.vs.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpecifikacijaOdgovor {

	@NotBlank
	private Integer id;

	@NotBlank
	private String naziv;

}
