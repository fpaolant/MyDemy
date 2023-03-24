package it.univaq.disim.mwt.mydemy.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Categoria extends BaseEntity {

	@NotBlank
	@Column(length = 30)
	@Size(max=30)
	@NotNull
	private String nome;

	@ManyToOne
	@JoinColumn(referencedColumnName = "id")
	@JsonBackReference
	private Categoria parent;

	@ManyToMany(mappedBy = "categorie")
	@JsonBackReference
	private Set<Corso> corsi = new HashSet<>();

	@OneToMany(mappedBy = "parent")
	@OrderBy("nome")
	private Set<Categoria> subCategorie;

	public Categoria() { super(); }

	public Categoria(@NotBlank String nome, Categoria parent) {
		super();
		this.nome = nome;
		this.parent = parent;
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
		final Categoria other = (Categoria) obj;
		if ((this.nome == null) ? (other.nome != null) : !(this.nome.equals(other.nome) && this.getId()==other.getId())) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Categoria [nome=" + nome + ", parentCategory=" + parent + ", id=" + getId() + "]";
	}
	

}
