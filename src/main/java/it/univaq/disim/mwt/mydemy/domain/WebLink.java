package it.univaq.disim.mwt.mydemy.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

@Entity
@Getter
@Setter
@ToString
public class WebLink extends BaseEntity {
	@Column(length = 150)
	@URL(regexp = "^(http|https).*")
	private String url;
	@NotBlank
	@Column(length = 30)
	@Size(max=30)
	private String name;
	
	public WebLink(@NotBlank String url, @NotBlank String name) {
		super();
		this.url = url;
		this.name = name;
	}
	

	public WebLink() {
		super();
	}
	
	 @Override
	    public int hashCode() {
	        int hash = 7;
	        hash = 29 * hash + (this.url != null ? this.url.hashCode() : 0);
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
	        final WebLink other = (WebLink) obj;
	        if ((this.url == null) ? (other.url != null) : 
	                !this.url.equals(other.url)) {
	            return false;
	        }
	        return true;
	    }

	
	
}
