package org.vs.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "proizvod")
public class Proizvod {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "kratak_naslov", nullable = false)
	private String kratakNaslov;

	@Column(name = "detaljan_opis", nullable = false)
	private String detaljanOpis;

	@Column(nullable = false)
	private double cijena;

	@Column(nullable = false)
	private boolean polovan;

	@Column(nullable = true)
	private String lokacija;

	@Column(name = "vrijeme_objave", nullable = false)
	private String vrijemeObjave;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "kategorija_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Kategorija kategorija;

	@Column(name = "status", nullable = false)
	private String status;

	@ManyToOne
	@JoinColumn(name = "vlasnik_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore	
	private Korisnik vlasnik;

	@OneToOne(mappedBy = "proizvod")
	private Kupovina kupovina;

}
