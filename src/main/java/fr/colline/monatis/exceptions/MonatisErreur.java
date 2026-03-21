package fr.colline.monatis.exceptions;

public interface MonatisErreur {

	public String getCode();
	public String getPrefixe();
	public String getPattern();
	public TypeErreur getTypeErreur();
	public TypeDomaine getTypeDomaine();

}
