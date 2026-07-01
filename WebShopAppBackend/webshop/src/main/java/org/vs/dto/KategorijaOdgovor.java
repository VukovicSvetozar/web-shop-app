package org.vs.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KategorijaOdgovor {

	@NotBlank
	private Integer id;

	@NotBlank
	private String naziv;

}
