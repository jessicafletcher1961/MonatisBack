package fr.colline.monatis.rapports.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.emprunts.model.ConditionEmprunt;
import fr.colline.monatis.emprunts.model.Echeance;
import fr.colline.monatis.emprunts.model.Emprunt;
import fr.colline.monatis.emprunts.service.EmpruntService;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.rapports.model.Echeancier;
import fr.colline.monatis.rapports.model.composants.echeancier.EcheancierConditionEmpruntLigne;
import fr.colline.monatis.rapports.model.composants.echeancier.EcheancierCumuls;
import fr.colline.monatis.rapports.model.composants.echeancier.EcheancierEcheanceLigne;
import fr.colline.monatis.rapports.model.composants.echeancier.EcheancierLigne;
import fr.colline.monatis.rapports.model.composants.echeancier.EcheancierPeriodeLigne;
import fr.colline.monatis.typologies.model.TypePeriode;
import fr.colline.monatis.utils.DateEtPeriodeUtils;

@Service
class EcheancierService {

	@Autowired private EmpruntService empruntService;

	Echeancier calculerEcheancier(
			Emprunt emprunt,
			LocalDate dateCible) throws ServiceException {

		TypePeriode typePeriode = TypePeriode.ANNUEL;
		ConditionEmprunt conditionEmpruntInitiale = emprunt.getConditionEmpruntInitiale();
		List<Echeance> echeances = empruntService.genererEcheances(emprunt);

		Echeancier echeancier = new Echeancier();
		echeancier.setEmprunt(emprunt);
		echeancier.setDateCible(dateCible);

		List<EcheancierLigne> lignes = new ArrayList<EcheancierLigne>();
		EcheancierCumuls cumulFinal = new EcheancierCumuls(conditionEmpruntInitiale.getDatePremiereEcheance());
		EcheancierCumuls cumulDateCible = null;
		if ( dateCible != null ) {
			cumulDateCible = new EcheancierCumuls(conditionEmpruntInitiale.getDatePremiereEcheance());
		}
		
		EcheancierConditionEmpruntLigne conditionEmpruntCouranteLigne = initialiserConditionEmpruntLigne(conditionEmpruntInitiale);
		lignes.add(conditionEmpruntCouranteLigne);
		
		EcheancierPeriodeLigne periodeCouranteLigne = initialiserPeriodeLigne(conditionEmpruntInitiale.getDatePremiereEcheance(), typePeriode);

		for ( Echeance echeance : echeances ) {
			if ( echeance.getDateEcheance().isAfter(periodeCouranteLigne.getDateFinPeriode()) ) {
				lignes.add(periodeCouranteLigne);
				periodeCouranteLigne = initialiserPeriodeLigne(echeance.getDateEcheance(), typePeriode); 
			}
			modifierCumuls(periodeCouranteLigne.getCumuls(), echeance);
			
			if ( ! echeance.getConditionEmprunt().getId().equals(conditionEmpruntCouranteLigne.getConditionEmprunt().getId()) ) {
				conditionEmpruntCouranteLigne = initialiserConditionEmpruntLigne(echeance.getConditionEmprunt());
				lignes.add(conditionEmpruntCouranteLigne);
			}
			modifierCumuls(conditionEmpruntCouranteLigne.getCumuls(), echeance);
			
			modifierCumuls(cumulFinal, echeance);
			if ( dateCible != null &&  ! echeance.getDateEcheance().isAfter(dateCible) ) {
				modifierCumuls(cumulDateCible, echeance);
			}
			
			EcheancierEcheanceLigne echeanceLigne = new EcheancierEcheanceLigne();
			echeanceLigne.setEcheance(echeance);
			echeanceLigne.setConditionEmpruntLigne(conditionEmpruntCouranteLigne);
			lignes.add(echeanceLigne);
		}
		lignes.add(periodeCouranteLigne);
		
		echeancier.setLignes(lignes);
		echeancier.setCumulFinal(cumulFinal);
		echeancier.setCumulDateCible(cumulDateCible);

		return echeancier;
	}

	private EcheancierConditionEmpruntLigne initialiserConditionEmpruntLigne(ConditionEmprunt conditionEmprunt) {
	
		EcheancierConditionEmpruntLigne ligne = new EcheancierConditionEmpruntLigne();
		
		ligne.setConditionEmprunt(conditionEmprunt);
		ligne.setCumuls(new EcheancierCumuls(conditionEmprunt.getDatePremiereEcheance()));

		return ligne;
	}

	private EcheancierPeriodeLigne initialiserPeriodeLigne(LocalDate dateCible, TypePeriode typePeriode) throws ServiceException {
		
		EcheancierPeriodeLigne ligne = new EcheancierPeriodeLigne();
		
		LocalDate dateDebutPeriode = DateEtPeriodeUtils.recadrerDateDebutPeriode(typePeriode, dateCible);
		LocalDate dateFinPeriode = DateEtPeriodeUtils.rechercherDateFinPeriode(typePeriode, dateDebutPeriode);
		
		ligne.setDateDebutPeriode(dateDebutPeriode);
		ligne.setDateFinPeriode(dateFinPeriode);
		ligne.setCumuls(new EcheancierCumuls(dateCible));
		
		return ligne;
	}

	private void modifierCumuls(EcheancierCumuls cumuls, Echeance echeance) {
		
		cumuls.setDateDerniereEcheance(echeance.getDateEcheance());
		cumuls.setNombreEcheances(cumuls.getNombreEcheances() + 1);
		
		cumuls.setCapitalEmprunteDejaRembourse(echeance.getCapitalEmprunteDejaRembourseEnCentimes());
		cumuls.setCapitalEmprunteRestantDu(echeance.getCapitalEmprunteRestantDuEnCentimes());

		cumuls.setCumulMontantPaiement(cumuls.getCumulMontantPaiement() + echeance.getMontantPaiementEnCentimes());
		cumuls.setCumulPartCapital(cumuls.getCumulPartCapital() + echeance.getPartCapitalEnCentimes());
		cumuls.setCumulPartInterets(cumuls.getCumulPartInterets() + echeance.getPartInteretsEnCentimes());
		cumuls.setCumulPartFraisFixes(cumuls.getCumulPartFraisFixes() + echeance.getPartFraisFixesEnCentimes());
	}
		
}
