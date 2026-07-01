package org.vs.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "proizvod_specifikacija")
public class ProizvodSpecifikacija {

	@EmbeddedId
	private ProizvodSpecifikacijaId id = new ProizvodSpecifikacijaId();

	@Column(name = "vrijednost")
	private String vrijednost;

}