package it.univaq.disim.mwt.mydemy.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;

@Entity
@Getter
@Setter
@ToString
public class Iscrizione extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
	private Corso corso;
	
	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
	@NotNull
	private Utente utente;
	private Boolean superato = false;
	@Lob
	private byte[] certificato;

	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime data = LocalDateTime.now();
	
	public Byte[] getCertificato() {
		if(this.superato) return null;
		return null;
	}
	
	public Iscrizione() {
		super();
	}
	
	public Iscrizione(Corso corso, Utente utente, Boolean superato, Boolean certificatoInviato, LocalDateTime data) {
		super();
		this.corso = corso;
		this.utente = utente;
		this.superato = superato;
		this.data = data;
	}
	
	@Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.corso.getId() + this.utente.getUsername());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Iscrizione other = (Iscrizione) obj;
        if (!Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        return true;
    }

}