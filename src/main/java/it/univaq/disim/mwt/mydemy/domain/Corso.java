package it.univaq.disim.mwt.mydemy.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import groovyjarjarantlr4.v4.runtime.misc.Nullable;

@Entity
@Getter
@Setter
public class Corso extends BaseEntity {
	@NotBlank
	@Column(length = 150)
	@NotBlank
	@Size(min=2, max=150)
	private String titolo;

	@Column(length = 1500)
	@Size(max=1500)
	private String descrizione;
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@Future
	private LocalDateTime inizio;
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm:ss")
	@Future
	private LocalDateTime fine;
	@Min(0) @Max(60)
	private Integer crediti;
	@Min(3) @Max(150)
	private Integer ore;
	@Digits(integer=6, fraction=2)
	private Float prezzo;

	private Boolean attestatoFinale = false;

	@NotNull
	@OneToOne
	private Utente creatore;

	@OneToMany(
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	private List<WebLink> links = new ArrayList<>();

	@Min(1) @Max(500)
	private int posti;
	private Boolean approvato = false;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
	@JoinTable(name = "corso_categoria", joinColumns = { @JoinColumn(name = "corso_id") }, inverseJoinColumns = {
			@JoinColumn(name = "categoria_id") })
	@JsonManagedReference
	private Set<Categoria> categorie = new HashSet<>();

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
	@JoinTable(name = "corso_tag", joinColumns = { @JoinColumn(name = "corso_id") }, inverseJoinColumns = {
			@JoinColumn(name = "tag_id") })
	@Nullable
	@JsonManagedReference
	private Set<Tag> tags = new HashSet<>();
	@Transient
	private Integer numIscritti;

	public Corso() { super(); }

	public Corso(@NotBlank @Size(max = 150) String titolo, String descrizione, LocalDateTime inizio, LocalDateTime fine,
			Integer crediti, Integer ore, Float prezzo, Boolean attestatoFinale, @NotNull Utente creatore,
			@Min(1) Integer posti, Boolean approvato) {
		super();
		this.titolo = titolo;
		this.descrizione = descrizione;
		this.inizio = inizio;
		this.fine = fine;
		this.crediti = crediti;
		this.ore = ore;
		this.prezzo = prezzo;
		this.attestatoFinale = attestatoFinale;
		this.creatore = creatore;
		this.posti = posti;
		this.approvato = approvato;
	}

	public void addWebLink(WebLink wl) {
		this.links.add(wl);
	}

	public void removeWebLink(long webLinkId) {
		WebLink wl = this.links.stream().filter(w -> w.getId() == webLinkId).findFirst().orElse(null);
		if (wl != null) {
			this.links.remove(wl);
		}
	}

	public void addTag(Tag tag) {
		this.tags.add(tag);
		tag.getCorsi().add(this);
	}

	public void removeTag(long tagId) {
		Tag tag = this.tags.stream().filter(t -> t.getId() == tagId).findFirst().orElse(null);
		if (tag != null) {
			this.tags.remove(tag);
			tag.getCorsi().remove(this);
		}
	}

	public void addCategoria(Categoria categoria) {
		this.categorie.add(categoria);
		categoria.getCorsi().add(this);
	}

	public void removeCategoria(long categoriaId) {
		Categoria categoria = this.categorie.stream().filter(c -> c.getId() == categoriaId).findFirst().orElse(null);
		if (categoria != null) {
			this.categorie.remove(categoria);
			//categoria.getCorsi().remove(this);
		}
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 89 * hash + Objects.hashCode(this.titolo);
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
		final Corso other = (Corso) obj;
		if (!Objects.equals(this.titolo, other.titolo)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Corso [titolo=" + titolo + ", descrizione=" + descrizione + ", inizio=" + inizio + ", fine=" + fine
				+ ", crediti=" + crediti + ", ore=" + ore + ", prezzo=" + prezzo + ", attestatoFinale="
				+ attestatoFinale + ", creatore=" + creatore + ", links=" + links + ", posti=" + posti + ", approvato="
				+ approvato + ", id=" + getId() + "]";
	}
	
	

}
