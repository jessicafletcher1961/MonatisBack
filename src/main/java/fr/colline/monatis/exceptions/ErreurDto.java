package fr.colline.monatis.exceptions;

import java.io.Serializable;

public class ErreurDto implements Serializable {

	private static final long serialVersionUID = 3235704918495067520L;

	public String type;
	public String code;
	public String libelle;
	public ErreurDto cause;
}
