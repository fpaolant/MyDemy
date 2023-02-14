package it.univaq.disim.mwt.mydemy.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
public class CreatoreInfo extends BaseEntity {
	
	private static final long serialVersionUID = 1L;
	
	@NotBlank
	@Column(length = 30)
    @Size(max=30)
	private String titolo;
	@Column(length = 255)
    @Size(max=255)
	private String descrizione;
    @OneToOne(mappedBy="creatoreInfo")
    @JsonIgnore
    private Utente utente;
	
	public CreatoreInfo() { super(); }
	
	public CreatoreInfo(@NotBlank @Size(max = 20) String titolo, String descrizione) {
		this.titolo = titolo;
		this.descrizione = descrizione;
        this.utente = utente;
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
        final CreatoreInfo other = (CreatoreInfo) obj;
        if (!Objects.equals(this.getId(), other.getId())) {
            return false;
        }
        return true;
    }	
    
    
	
}
