package it.univaq.disim.mwt.mydemy.domain;


import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@ToString
public class Utente extends BaseEntity {

	private static final long serialVersionUID = 1L;


	@Column(length = 30)
	@Size(max=30)
	private String nome;
	@NotBlank
	@Column(length = 30)
	@Size(max=30)
	private String cognome;
	@Email
	@Column(length = 50)
	@Size(max=50)
	private String email;
	@Lob
	private byte[] foto;
	@Column(nullable = false, unique = true, length = 30)
	@Size(min=4, max=30)
	private String username;
	@Column(length = 60)
	@Size(max=60)
	@NotBlank
	@NotNull
	private String password;

	private Boolean enabled = true;

	@OneToOne(cascade = CascadeType.ALL)
	private CreatoreInfo creatoreInfo;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE })
	@JoinTable(name = "utente_ruolo", joinColumns = { @JoinColumn(name = "utente_id") }, inverseJoinColumns = {
			@JoinColumn(name = "ruolo_id") })
	private Set<Ruolo> ruoli = new HashSet<>();
	@OneToMany(mappedBy = "utente")
	@JsonBackReference
	Set<Iscrizione> iscrizioni;

	public Utente() { super(); }

	public Utente(@NotBlank String nome, @NotBlank String cognome, @Email String email, @NotBlank String username,
			@NotBlank String password, @NotNull Boolean enabled) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}

	public void addRuolo(Ruolo r) {
		this.ruoli.add(r);
	}

	public void removeRuolo(long roleId) {
		Ruolo ruolo = this.ruoli.stream().filter(r -> r.getId() == roleId).findFirst().orElse(null);
		if (ruolo != null) {
			this.ruoli.remove(ruolo);
		}
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 89 * hash + Objects.hashCode(this.username);
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
		final Utente other = (Utente) obj;
		if (!Objects.equals(this.username, other.username)) {
			return false;
		}
		return true;
	}

}
