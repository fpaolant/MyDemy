package it.univaq.disim.mwt.mydemy.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Getter
@Setter
@ToString
public class Ordine  extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne(cascade = { CascadeType.MERGE})
	private Utente ordinante;
	
	private Integer totale;
	
	@Enumerated(EnumType.STRING)
	private StatoOrdine stato;
	
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime data;
	
	public Ordine( ) {}
	public Ordine(@NotNull Utente ordinante, Integer totale, StatoOrdine stato, LocalDateTime data) {
		super();
		this.ordinante = ordinante;
		this.totale = totale;
		this.stato = stato;
		this.data = data;
	}
	
	@Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.getId());
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
        final Ordine other = (Ordine) obj;
        if (!Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        return true;
    }

}
