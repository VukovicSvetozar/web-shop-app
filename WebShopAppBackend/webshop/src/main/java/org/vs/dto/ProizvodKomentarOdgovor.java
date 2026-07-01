package org.vs.dto;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProizvodKomentarOdgovor {

	@NotNull
	private Integer id;

	@NotBlank
	@Size(min = 3, max = 300)
	private String komentar;

//	@NotNull
//	@PastOrPresent
//	private String vrijeme;

	private String avatarUrl;

	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9_]*$")
	@Size(min = 3, max = 100)
	private String korisnickoIme;

	@NotNull
	private Integer proizvodId;

}
