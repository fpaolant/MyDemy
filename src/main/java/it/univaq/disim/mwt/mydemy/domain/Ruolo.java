package it.univaq.disim.mwt.mydemy.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Ruolo extends BaseEntity {

	@Column(length = 30)
    @NotBlank
    @Size(max=30)
	private String nome;
    @NotBlank
	@Column(length = 10)
    @Size(max=10)
	private String code;
	@Column(length = 100)
    @Size(max=100)
	private String descrizione;
	
	
	public Ruolo() { super(); }
	public Ruolo(String nome, String code, String descrizione) {
        super();
		this.nome = nome;
		this.code = code;
		this.descrizione = descrizione;
	}
	
	@Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.nome);
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
        final Ruolo other = (Ruolo) obj;
        if (!Objects.equals(this.nome, other.nome)) {
            return false;
        }
        return true;
    }

}
