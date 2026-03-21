package fr.colline.monatis.exceptions.interfaces;

import fr.colline.monatis.exceptions.erreurs.TypeErreur;

public interface MonatisExceptionInterface {

	public MonatisErreurInterface getErreur();
	
	public TypeErreur getType();
	
	public String getCode();
	
	public Object[] getValues();
}
