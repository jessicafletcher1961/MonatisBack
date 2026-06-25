package fr.colline.monatis.operations.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.colline.monatis.comptes.model.Compte;
import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.service.CompteExterneService;
import fr.colline.monatis.comptes.service.CompteInterneService;
import fr.colline.monatis.comptes.service.CompteTechniqueService;
import fr.colline.monatis.exceptions.GeneriqueTechniqueErreur;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.typologies.model.TypeCompte;
import fr.colline.monatis.typologies.model.TypeFonctionnement;
import fr.colline.monatis.typologies.model.TypeOperation;

@Service
public class CompatibiliteService {

	private record CaracteristiqueCompte(TypeCompte typeCompte, TypeFonctionnement typeFonctionnement) {};

	@Autowired private CompteInterneService compteInterneService;
	@Autowired private CompteExterneService compteExterneService;
    @Autowired private CompteTechniqueService compteTechniqueService;
    
	public boolean isCompatibiliteDepense(Compte compte, TypeOperation typeOperation) throws ServiceException {
		
		CaracteristiqueCompte caracteristiqueCompte = determinerCaracteristiqueCompte(compte);
		CaracteristiqueCompte caracteristiqueTypeOperation = determinerCaracteristiqueCompteCompatibleDepense(typeOperation);
		
		return caracteristiqueCompte.equals(caracteristiqueTypeOperation);
	}

	public boolean isCompatibiliteRecette(Compte compte, TypeOperation typeOperation) throws ServiceException {
		
		CaracteristiqueCompte caracteristiqueCompte = determinerCaracteristiqueCompte(compte);
		CaracteristiqueCompte caracteristiqueTypeOperation = determinerCaracteristiqueCompteCompatibleRecette(typeOperation);
		
		return caracteristiqueCompte.equals(caracteristiqueTypeOperation);
	}
	
	public List<TypeOperation> rechercherTypesOperationCompatiblesDepense(Compte compte) throws ServiceException {
		
		List<TypeOperation> resultat = new ArrayList<TypeOperation>();
		
		for ( TypeOperation typeOperation : TypeOperation.values() ) {
			if ( isCompatibiliteDepense(compte, typeOperation) ) {
				resultat.add(typeOperation);
			}
		}
		
		return resultat;
	}	
	
	public List<TypeOperation> rechercherTypesOperationCompatiblesRecette(Compte compte) throws ServiceException {

		List<TypeOperation> resultat = new ArrayList<TypeOperation>();
		
		for ( TypeOperation typeOperation : TypeOperation.values() ) {
			if ( isCompatibiliteRecette(compte, typeOperation) ) {
				resultat.add(typeOperation);
			}
		}
		
		return resultat;
	}
    
	public List<Compte> rechercherComptesCompatiblesDepense(TypeOperation typeOperation) throws ServiceException {

		return rechercherComptesParCaracteristique(determinerCaracteristiqueCompteCompatibleDepense(typeOperation));
	}

	public List<Compte> rechercherComptesCompatiblesRecette(TypeOperation typeOperation) throws ServiceException {

		return rechercherComptesParCaracteristique(determinerCaracteristiqueCompteCompatibleRecette(typeOperation));
	}
	
	private CaracteristiqueCompte determinerCaracteristiqueCompte(Compte compte) {

		TypeCompte typeCompte = compte.getTypeCompte();
		TypeFonctionnement typeFonctionnement = null;
		
		if ( CompteInterne.class.isAssignableFrom(compte.getClass()) ) {
			CompteInterne compteInterne = (CompteInterne) compte;
			typeFonctionnement = compteInterne.getTypeFonctionnement();
		}
		
		return new CaracteristiqueCompte(typeCompte, typeFonctionnement);
	}
	
	private List<Compte> rechercherComptesParCaracteristique(CaracteristiqueCompte caracteristique) throws ServiceException {
		
		List<Compte> resultat = new ArrayList<>();
		
		switch (caracteristique.typeCompte) {
		case INTERNE:
			resultat.addAll(compteInterneService.rechercherParTypeFonctionnement(caracteristique.typeFonctionnement));
			break;
		case EXTERNE:	
			resultat.addAll(compteExterneService.rechercherTous());
			break;
		case TECHNIQUE:
			resultat.add(compteTechniqueService.rechercherOuCreerCompteTechnique());
			break;
		default:
			throw new ServiceException(GeneriqueTechniqueErreur.TYPE_NON_GERE,
					TypeCompte.class.getSimpleName(),
					caracteristique.typeCompte.getCode(),
					caracteristique.typeCompte.getLibelle());
		}
		
		return resultat;
	}

	private CaracteristiqueCompte determinerCaracteristiqueCompteCompatibleDepense(TypeOperation typeOperation) throws ServiceException {

		CaracteristiqueCompte resultat;

		switch(typeOperation) {

		case TRANSFERT:
		case DEPOT:
		case DEPENSE:
		case FRAIS_COMPTE_COURANT:
			resultat = new CaracteristiqueCompte(TypeCompte.INTERNE, TypeFonctionnement.COURANT);
			break;
		case RETRAIT:
		case FRAIS_COMPTE_FINANCIER:
			resultat = new CaracteristiqueCompte(TypeCompte.INTERNE, TypeFonctionnement.FINANCIER);
			break;
		case VENTE:
		case FRAIS_COMPTE_BIEN:
			resultat = new CaracteristiqueCompte(TypeCompte.INTERNE, TypeFonctionnement.BIEN);
			break;
		case RECETTE:
		case ACHAT:
			resultat = new CaracteristiqueCompte(TypeCompte.EXTERNE, null);
			break;
		case REMUNERATION_COMPTE_COURANT:
		case REMUNERATION_COMPTE_FINANCIER:
		case REMUNERATION_COMPTE_BIEN:
			resultat = new CaracteristiqueCompte(TypeCompte.TECHNIQUE, null);
			break; 
		default:
			throw new ServiceException(GeneriqueTechniqueErreur.TYPE_NON_GERE,
					TypeOperation.class.getSimpleName(),
					typeOperation.getCode(),
					typeOperation.getLibelle());
		}
		
		return resultat;
	}

	private CaracteristiqueCompte determinerCaracteristiqueCompteCompatibleRecette(TypeOperation typeOperation) throws ServiceException {

		CaracteristiqueCompte resultat;
		switch(typeOperation) {

		case TRANSFERT:
		case RETRAIT:
		case RECETTE:
		case REMUNERATION_COMPTE_COURANT:
			resultat = new CaracteristiqueCompte(TypeCompte.INTERNE, TypeFonctionnement.COURANT);
			break;
		case DEPOT:
		case REMUNERATION_COMPTE_FINANCIER:
			resultat = new CaracteristiqueCompte(TypeCompte.INTERNE, TypeFonctionnement.FINANCIER);
			break;
		case ACHAT:
		case REMUNERATION_COMPTE_BIEN:
			resultat = new CaracteristiqueCompte(TypeCompte.INTERNE, TypeFonctionnement.BIEN);
			break;
		case DEPENSE:
		case VENTE:
			resultat = new CaracteristiqueCompte(TypeCompte.EXTERNE, null);
			break;
		case FRAIS_COMPTE_COURANT:
		case FRAIS_COMPTE_FINANCIER:
		case FRAIS_COMPTE_BIEN:
			resultat = new CaracteristiqueCompte(TypeCompte.TECHNIQUE, null);
			break;
		default:
			throw new ServiceException(GeneriqueTechniqueErreur.TYPE_NON_GERE,
					TypeOperation.class.getSimpleName(),
					typeOperation.getCode(),
					typeOperation.getLibelle());
		}

		return resultat;
	}

}
