package fr.colline.monatis.rapports.controller.plus_moins_values;

import java.io.Serializable;
import java.util.List;

import fr.colline.monatis.rapports.controller.releve_compte.EnteteCompteResponseDto;

public class HistoriquePlusMoinsValueResponseDto implements Serializable {

	private static final long serialVersionUID = -1477690966457581282L;

	public EnteteCompteResponseDto enteteCompte;
	
	public List<PlusMoinsValueResponseDto> plusMoinsValues;
}
