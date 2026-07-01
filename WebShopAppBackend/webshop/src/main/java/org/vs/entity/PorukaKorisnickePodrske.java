package org.vs.entity;

import org.hibernate.annotations.*;

import com.fasterxml.jackson.annotation.*;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "poruka_podrska")
public class PorukaKorisnickePodrske {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String naslovPoruke;

	@Column(nullable = false)
	private String tekstPoruke;

	@Column(name = "vrijeme_poruke", nullable = false)
	private String vrijemePoruke;

	private String naslovOdgovor;

	private String tekstOdgovor;

	@Column(name = "vrijeme_odgovor")
	private String vrijemeOdgovor;

	@Column(nullable = false)
	private boolean procitana;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "korisnik_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Korisnik korisnik;

}
