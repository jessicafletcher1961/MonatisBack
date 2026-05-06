package fr.colline.monatis.operations.repository;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fr.colline.monatis.operations.model.OperationLigne;

@Repository
public interface OperationLigneRepository extends JpaRepository<OperationLigne, Long> {

	@Query(value = "select o from OperationLigne o"
			+ " where (:sousCategorieId = o.sousCategorie.id)"
			+ " and (:beneficiaireId is null or :beneficiaireId in (select b.id from o.beneficiaires b))"
			+ " and (:dateDebut is null or o.dateComptabilisation >= :dateDebut)"
			+ " and (:dateFin is null or o.dateComptabilisation <= :dateFin)"
			+ " order by o.dateComptabilisation desc")
	public Stream<OperationLigne> findBySousCategorieIdAndFilters(
			Long sousCategorieId,
			Long beneficiaireId,
			LocalDate dateDebut,
			LocalDate dateFin);

	@Query(value = "select o from OperationLigne o"
			+ " where (:beneficiaireId in (select b.id from o.beneficiaires b))"
			+ " and (:sousCategorieId is null or :sousCategorieId = o.sousCategorie.id)"
			+ " and (:dateDebut is null or o.dateComptabilisation >= :dateDebut)"
			+ " and (:dateFin is null or o.dateComptabilisation <= :dateFin)"
			+ " order by o.dateComptabilisation desc")
	public Stream<OperationLigne> findByBeneficiaireIdAndFilters(
			Long beneficiaireId,
			Long sousCategorieId,
			LocalDate dateDebut,
			LocalDate dateFin);
}
