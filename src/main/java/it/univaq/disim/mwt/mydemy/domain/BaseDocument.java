package it.univaq.disim.mwt.mydemy.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
public abstract class BaseDocument {
	@Id
	private String index;

	@Version
	private long version;
}
