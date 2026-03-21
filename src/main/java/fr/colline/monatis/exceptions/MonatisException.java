package fr.colline.monatis.exceptions;

public interface MonatisException {

	public MonatisErreur getErreur();
	public Object[] getValues();

}
