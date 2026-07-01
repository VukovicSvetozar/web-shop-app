package org.vs.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PorukaKorisnickePodrskeOdgovor {

	@NotNull
	private Integer id;

	@NotBlank
	@Size(min = 3, max = 30)
	private String naslovPoruke;

	@NotBlank
	@Size(min = 3, max = 300)
	private String tekstPoruke;

	@NotNull
	@PastOrPresent
	private LocalDateTime vrijemePoruke;

	@Size(min = 3, max = 30)
	private String naslovOdgovor;

	@Size(min = 3, max = 300)
	private String tekstOdgovor;

	@PastOrPresent
	private LocalDateTime vrijemeOdgovor;

	private boolean procitana;

	@NotBlank
	@Pattern(regexp = "^[a-zA-Z0-9_]*$")
	@Size(min = 3, max = 100)
	private String korisnickoIme;

	@NotBlank
	@Email
	@Size(max = 100)
	private String email;

}
