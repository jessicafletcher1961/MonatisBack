package fr.colline.monatis.exceptions;

import fr.colline.monatis.erreurs.MonatisErreur;

public interface MonatisException {

	public MonatisErreur getErreur();
	public Object[] getValues();

}
