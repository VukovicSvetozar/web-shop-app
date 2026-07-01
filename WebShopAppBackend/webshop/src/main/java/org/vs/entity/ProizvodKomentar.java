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
@Table(name = "proizvod_komentar")
public class ProizvodKomentar {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String komentar;

	@Column(nullable = false)
	private String vrijeme;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "korisnik_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Korisnik korisnik;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "proizvod_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Proizvod proizvod;

}
