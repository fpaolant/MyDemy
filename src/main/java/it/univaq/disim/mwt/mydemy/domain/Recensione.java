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
public class Recensione extends BaseDocument {
	
	@NotBlank
	@Size(min = 3, max = 100)
	private String titolo;
	@Size(max = 1000)
	private String descrizione;
	@Min(1)
	@Max(5)
	private int voto;
	private LocalDateTime dataImmissione = LocalDateTime.now();
	private String autore;
	private Long autoreId;
	private Long corsoId;
	
	public Recensione() { super(); }

	public Recensione(String titolo, String descrizione, int voto, LocalDateTime dataImmissione, String autore, Long utenteId, Long corsoId) {
		super();
		this.titolo = titolo;
		this.descrizione = descrizione;
		this.voto = voto;
		this.dataImmissione = dataImmissione;
		this.autore = autore;
		this.autoreId = utenteId;
		this.corsoId = corsoId;
	}


}


