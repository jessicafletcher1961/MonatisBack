package fr.colline.monatis.exceptions;

import fr.colline.monatis.exceptions.erreurs.TypeErreur;
import fr.colline.monatis.exceptions.interfaces.MonatisErreurInterface;
import fr.colline.monatis.exceptions.interfaces.MonatisExceptionInterface;

public class ServiceException 
extends Exception
implements MonatisExceptionInterface {

	private static final long serialVersionUID = -1850444732930288165L;

	private MonatisErreurInterface erreur;
	
	private Object[] values;

	@Override
	public MonatisErreurInterface getErreur() {
		return erreur;
	}

	@Override
	public TypeErreur getType() {
		return erreur.getType();
	}

	@Override
	public String getCode() {
		return erreur.getCode();
	}

	@Override
	public Object[] getValues() {
		return values;
	}

	public ServiceException(
			MonatisErreurInterface erreur,
			Object...values) {

		super(erreur.getMessage(values));

		this.erreur = erreur;
		this.values = values;
	}

	public ServiceException(
			Throwable cause,
			MonatisErreurInterface erreur,
			Object...values) {

		super(erreur.getMessage(values), cause);

		this.erreur = erreur;
		this.values = values;
	}
}
