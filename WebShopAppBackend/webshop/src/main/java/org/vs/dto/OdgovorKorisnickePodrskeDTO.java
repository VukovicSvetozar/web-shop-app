package org.vs.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OdgovorKorisnickePodrskeDTO {

	private Integer id;
	
	@NotBlank
	@Size(min = 3, max = 30)
	private String naslov;
	
	@NotBlank
	@Size(min = 3, max = 300)
	private String tekst;
	
	@NotNull
	@PastOrPresent
	private LocalDateTime vrijeme;
	
	@NotBlank
	@Email
	@Size(max = 100)
	private String email;

}
