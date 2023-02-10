package it.univaq.disim.mwt.mydemy.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Categoria extends BaseEntity {

	private static final long serialVersionUID = 1L;

	@NotBlank
	@Column(length = 30)
	private String nome;

	@ManyToOne(cascade = { CascadeType.MERGE })
	@JoinColumn(referencedColumnName = "id")
	@JsonBackReference
	private Categoria parent;

	@Column(length = 30)
	private String icon = "fa-sharp fa-solid fa-list";

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE }, mappedBy = "categorie")
	@JsonBackReference
	private Set<Corso> corsi = new HashSet<>();

	public Categoria() {
	}

	public Categoria(@NotBlank String nome, Categoria parent, String icon) {
		super();
		this.nome = nome;
		this.parent = parent;
		this.icon = icon;
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
		return "Categoria [nome=" + nome + ", parentCategory=" + parent + ", icon=" + icon + ", id=" + getId() + "]";
	}
	

}
