package fr.colline.monatis.exceptions;

import fr.colline.monatis.erreurs.MonatisErreur;

public class ControllerException extends Exception implements MonatisException {

	private static final long serialVersionUID = 7854587918226619571L;

	private MonatisErreur erreur;
	
	private Object[] values;
	
	@Override
	public MonatisErreur getErreur() {
		return erreur;
	}

	@Override
	public Object[] getValues() {
		return values;
	}

	public ControllerException(MonatisErreur erreur, Object...values) {
		
		super(String.format(erreur.getPattern(), values));
		
		this.erreur = erreur;
		this.values = values;
	}
	
	public ControllerException(Throwable t, MonatisErreur erreur, Object...values) {
		
		super(String.format(erreur.getPattern(), values), t);
		
		this.erreur = erreur;
		this.values = values;
	}
}
