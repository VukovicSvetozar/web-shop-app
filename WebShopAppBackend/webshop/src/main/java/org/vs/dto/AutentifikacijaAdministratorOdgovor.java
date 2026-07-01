package org.vs.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AutentifikacijaAdministratorOdgovor {

	@NotBlank
	private String jwtToken;

}
