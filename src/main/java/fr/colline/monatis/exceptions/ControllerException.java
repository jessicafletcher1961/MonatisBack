package fr.colline.monatis.exceptions;

import fr.colline.monatis.exceptions.erreurs.TypeErreur;
import fr.colline.monatis.exceptions.interfaces.MonatisErreurInterface;
import fr.colline.monatis.exceptions.interfaces.MonatisExceptionInterface;

public class ControllerException 
extends Exception
implements MonatisExceptionInterface {

	private static final long serialVersionUID = -6274042649774721255L;

	private MonatisErreurInterface erreur;

	private Object[] values;

	@Override 
	public MonatisErreurInterface getErreur() {
		return erreur;
	}
	
	@Override
	public Object[] getValues() {
		return values;
	}

	@Override
	public TypeErreur getType() {
		return erreur.getType();
	}

	@Override
	public String getCode() {
		return erreur.getCode();
	}

	public ControllerException(
			MonatisErreurInterface erreur,
			Object...values) {
		
		super(erreur.getMessage(values));

		this.erreur = erreur;
		this.values = values;
	}
	
	public ControllerException(
			Throwable t,
			MonatisErreurInterface erreur,
			Object...values) {
		
		super(erreur.getMessage(values), t);

		this.erreur = erreur;
		this.values = values;
	}
}
