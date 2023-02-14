package it.univaq.disim.mwt.mydemy.domain;


import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;


@Getter
@Setter
public class Recensione extends BaseEntity {
	
	@NotBlank
	private String titolo;
	private String descrizione;
	@Min(0)
	@Max(5)
	private Integer voto;
	private Boolean approvata;
	private LocalDateTime dataImmissione;
	@NotNull
	private Utente autore; 
	
	public Recensione() { super(); }
}
