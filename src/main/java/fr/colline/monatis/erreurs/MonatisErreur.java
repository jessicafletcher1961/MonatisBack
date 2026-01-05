package fr.colline.monatis.erreurs;

public interface MonatisErreur {

	public String getCode();
	public String getPrefixe();
	public String getPattern();
	public TypeErreur getTypeErreur();
	public TypeDomaine getTypeDomaine();

}
