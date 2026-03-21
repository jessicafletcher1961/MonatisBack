package fr.colline.monatis.rapports.controller.plus_moins_values;

import java.io.Serializable;

import fr.colline.monatis.rapports.controller.commun.EnteteCompteResponseDto;

public class PlusMoinsValueCompteInterneLigneResponseDto implements Serializable {

	private static final long serialVersionUID = -1477690966457581282L;

	public EnteteCompteResponseDto compteInterne;

	public PlusMoinsValueCompteInternePeriodeResponseDto[] periodes;
}
