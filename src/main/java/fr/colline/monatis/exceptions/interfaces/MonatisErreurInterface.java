package fr.colline.monatis.exceptions.interfaces;

import fr.colline.monatis.exceptions.erreurs.TypeErreur;

public interface MonatisErreurInterface {
	
	public TypeErreur getType();
	
	public String getCode();
	
	public String getMessage(Object[] values);
}
