package org.vs.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "kupovina")
public class Kupovina {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(nullable = false, name = "vrijeme_kupovine")
	private String vrijemeKupovine;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "placanje_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Placanje placanje;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "proizvod_id")
	private Proizvod proizvod;

	@ManyToOne
	@JoinColumn(name = "prodavac_id")
	private Korisnik prodavac;

	@ManyToOne
	@JoinColumn(name = "kupac_id")
	private Korisnik kupac;

}
