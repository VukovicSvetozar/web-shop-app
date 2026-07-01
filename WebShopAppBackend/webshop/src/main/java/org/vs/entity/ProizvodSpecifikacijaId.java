package org.vs.entity;

import java.io.Serializable;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class ProizvodSpecifikacijaId implements Serializable {

	private static final long serialVersionUID = -2343536857390097412L;

	@NonNull
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId
	@JoinColumn(name = "proizvod_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Proizvod proizvod;

	@NonNull
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId
	@JoinColumn(name = "specifikacija_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Specifikacija specifikacija;

}