package it.univaq.disim.mwt.mydemy.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import groovyjarjarantlr4.v4.runtime.misc.Nullable;

@Entity
@Getter
@Setter
public class Tag extends BaseEntity {

	@Column(nullable = false, unique = true, length = 15)
	@NotNull
	@NotBlank
	@Size(max=15)
	private String nome;

	@ManyToMany(cascade = { CascadeType.MERGE }, mappedBy = "tags")
	@Nullable
	@JsonBackReference
	private Set<Corso> corsi = new HashSet<>();

	public Tag() {
		super();
	}

	public Tag(@NotBlank String nome) {
		super();
		this.nome = nome;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 29 * hash + (this.nome != null ? (this.nome+this.getId()).hashCode() : 0);
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
		final Tag other = (Tag) obj;
		if ((this.nome == null) ? (other.nome != null) : !(this.nome.equals(other.nome) && this.getId()==other.getId())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Tag [nome=" + nome + ", corsi=" + corsi + ", id=" + getId() + "]";
	}
	
	

}
