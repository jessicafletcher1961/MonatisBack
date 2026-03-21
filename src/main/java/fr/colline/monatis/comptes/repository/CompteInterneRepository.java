package fr.colline.monatis.comptes.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.TypeFonctionnement;

@Repository
public interface CompteInterneRepository extends CompteRepository<CompteInterne> {

	public List<CompteInterne> findByTypeFonctionnementOrderByIdentifiant(TypeFonctionnement typeFonctionnement);

	@Query(nativeQuery = true,
			value = "select count(1) from evaluation where compte_interne_id = :id")
	public int compterEvaluationByCompteId(@Param("id") Long compteId);	

	@Query(nativeQuery = true,
			value = "select count(1) from operation where (compte_depense_id = :id or compte_recette_id = :id) and operation.date_valeur > :date")
	public int compterOperationByCompteIdApresDate(
			@Param("id") Long compteId, 
			@Param("date") LocalDate date);	

}
