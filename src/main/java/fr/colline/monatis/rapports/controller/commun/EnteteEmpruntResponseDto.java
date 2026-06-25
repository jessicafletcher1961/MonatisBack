package fr.colline.monatis.rapports.controller.commun;

import java.io.Serializable;
import java.time.LocalDateTime;

import fr.colline.monatis.typologies.controller.TypologieResponseDto;

public class EnteteEmpruntResponseDto implements Serializable {

	private static final long serialVersionUID = -1897778886982591547L;

	public LocalDateTime momentCalcul;

	public BanqueResponseDto banque;

	public String cle;

	public String libelle;

	public double montantPretEnEuros;

	public int dureePret;

	public TypologieResponseDto periodiciteEcheances;

}
