package fr.colline.monatis.exceptions;

import fr.colline.monatis.erreurs.MonatisErreur;

public class ServiceException extends Exception implements MonatisException {

	private static final long serialVersionUID = 4984788606363883352L;

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

	public ServiceException(MonatisErreur erreur, Object...values) {
		
		super(String.format(erreur.getPattern(), values));
		
		this.erreur = erreur;
		this.values = values;
	}

	public ServiceException(Throwable t, MonatisErreur erreur, Object...values) {
		
		super(String.format(erreur.getPattern(), values), t);
		
		this.erreur = erreur;
		this.values = values;
	}
}
