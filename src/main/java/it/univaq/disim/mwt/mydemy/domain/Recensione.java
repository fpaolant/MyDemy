package it.univaq.disim.mwt.mydemy.domain;


import javax.validation.constraints.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Document(collection = "recensioni")
public class Recensione extends BaseEntity {
	
	@NotBlank
	@Size(min = 3, max = 100)
	private String titolo;
	@Size(max = 1000)
	private String descrizione;
	@Min(0)
	@Max(5)
	private Integer voto;
	private LocalDateTime dataImmissione = LocalDateTime.now();
	@NotNull
	private Utente autore;
	@NotNull
	private Corso corso;
	
	public Recensione() { super(); }

	public Recensione(String titolo, String descrizione, Integer voto, LocalDateTime dataImmissione, Utente autore, Corso corso) {
		super();
		this.titolo = titolo;
		this.descrizione = descrizione;
		this.voto = voto;
		this.dataImmissione = dataImmissione;
		this.autore = autore;
		this.corso = corso;
	}
}
