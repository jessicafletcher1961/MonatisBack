package fr.colline.monatis.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import fr.colline.monatis.exceptions.GeneriqueTechniqueErreur;
import fr.colline.monatis.exceptions.ServiceException;
import fr.colline.monatis.typologies.model.TypePeriode;

public class DateEtPeriodeUtils {

	public static LocalDate recadrerDateDebutPeriode(TypePeriode typePeriode, LocalDate dateCible) throws ServiceException {

		LocalDate dateDebutPeriode;
		LocalDate date1, date2, date3, date4;

		switch(typePeriode) {
		case ANNUEL:
			dateDebutPeriode = LocalDate.of(
					dateCible.getYear(),
					1, 1);
			break;
		case SEMESTRIEL:
			date1 = LocalDate.of(
					dateCible.getYear(),
					1, 1);
			date2 = LocalDate.of(
					dateCible.getYear(),
					7, 1);
			if ( dateCible.isBefore(date2) )  {
				dateDebutPeriode = date1;
				break;
			}
			dateDebutPeriode = date2;
			break;
		case QUADRIMESTRIEL:
			date1 = LocalDate.of(
					dateCible.getYear(),
					1, 1);
			date2 = LocalDate.of(
					dateCible.getYear(),
					5, 1);
			if ( dateCible.isBefore(date2) )  {
				dateDebutPeriode = date1;
				break;
			}
			date3 = LocalDate.of(
					dateCible.getYear(),
					9, 1);
			if ( dateCible.isBefore(date3) )  {
				dateDebutPeriode = date2;
				break;
			}
			dateDebutPeriode = date3;
			break;
		case TRIMESTRIEL:
			date1 = LocalDate.of(
					dateCible.getYear(),
					1, 1);
			date2 = LocalDate.of(
					dateCible.getYear(),
					4, 1);
			if ( dateCible.isBefore(date2)  ) {
				dateDebutPeriode = date1;
				break;
			}
			date3 = LocalDate.of(
					dateCible.getYear(),
					7, 1);
			if ( dateCible.isBefore(date3)  ) {
				dateDebutPeriode = date2;
				break;
			}
			date4 = LocalDate.of(
					dateCible.getYear(),
					10, 1);
			if ( dateCible.isBefore(date4) ) {
				dateDebutPeriode = date3;
				break;
			}
			dateDebutPeriode = date4;
			break;
		case BIMESTRIEL:
			if ( dateCible.getMonthValue() % 2 == 0 ) {
				dateDebutPeriode = LocalDate.of(dateCible.getYear(), dateCible.getMonthValue() - 1 , 1);
			}
			else {
				dateDebutPeriode = LocalDate.of(dateCible.getYear(), dateCible.getMonthValue(), 1);
			}
			break;
		case MENSUEL:
			dateDebutPeriode = LocalDate.of(
					dateCible.getYear(),
					dateCible.getMonthValue(), 
					1);
			break;
		default:
			throw new ServiceException(
					GeneriqueTechniqueErreur.TYPE_NON_GERE,
					TypePeriode.class.getSimpleName(),
					typePeriode.getCode(),
					typePeriode.getLibelle());
		}

		return dateDebutPeriode;
	}

	public static LocalDate rechercherDebutPeriodeSuivante(TypePeriode typePeriode, LocalDate dateCible) throws ServiceException {

		LocalDate dateDebutPeriodeCourante = DateEtPeriodeUtils.recadrerDateDebutPeriode(typePeriode, dateCible);
		LocalDate dateDebutPeriodeSuivante;

		switch(typePeriode) {
		case ANNUEL:
			dateDebutPeriodeSuivante = dateDebutPeriodeCourante.plusYears(1);
			break;
		case SEMESTRIEL:
			dateDebutPeriodeSuivante = dateDebutPeriodeCourante.plusMonths(6);
			break;
		case QUADRIMESTRIEL:
			dateDebutPeriodeSuivante = dateDebutPeriodeCourante.plusMonths(4);
			break;
		case TRIMESTRIEL:
			dateDebutPeriodeSuivante = dateDebutPeriodeCourante.plusMonths(3);
			break;
		case BIMESTRIEL:
			dateDebutPeriodeSuivante = dateDebutPeriodeCourante.plusMonths(2);
			break;
		case MENSUEL:
			dateDebutPeriodeSuivante = dateDebutPeriodeCourante.plusMonths(1);
			break;
		default:
			throw new ServiceException(
					GeneriqueTechniqueErreur.TYPE_NON_GERE,
					TypePeriode.class.getSimpleName(),
					typePeriode.getCode(),
					typePeriode.getLibelle());
		}

		return dateDebutPeriodeSuivante;
	}

	public static LocalDate rechercherDateFinPeriode(TypePeriode typePeriode, LocalDate dateDebutPeriode) throws ServiceException {
		
		return rechercherDebutPeriodeSuivante(typePeriode, dateDebutPeriode).minus(1, ChronoUnit.DAYS);
	}

	public static long calculerNombreMoisEntreDeuxDates(LocalDate dateDebut, LocalDate dateFin) {

		return ChronoUnit.MONTHS.between(dateDebut, dateFin.plus(1, ChronoUnit.DAYS));
	}

	public static int calculerNombrePeriodesEntreDateDebutEtDateFin(
			TypePeriode typePeriode,
			LocalDate dateDebut, 
			LocalDate dateFin) throws ServiceException {

		if ( typePeriode == null ) {
			return 1;
		}
		
		int nombrePeriode = 0;
		LocalDate dateDebutPeriode = recadrerDateDebutPeriode(typePeriode, dateDebut);
		
		while ( ! dateDebutPeriode.isAfter(dateFin) ) {
			nombrePeriode++;
			dateDebutPeriode = rechercherDebutPeriodeSuivante(typePeriode, dateDebutPeriode);
		}
		
		return nombrePeriode;
	}

}
