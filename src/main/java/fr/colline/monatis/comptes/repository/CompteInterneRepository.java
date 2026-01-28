package fr.colline.monatis.comptes.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.colline.monatis.comptes.model.CompteInterne;
import fr.colline.monatis.comptes.model.TypeFonctionnement;

@Repository
public interface CompteInterneRepository extends CompteRepository<CompteInterne> {

	public Set<CompteInterne> findByTypeFonctionnement(TypeFonctionnement typeFonctionnement);

	@Query(nativeQuery = true,
			value = "select count(1) from evaluation where compte_interne_id = :id")
	public int compterEvaluationByCompteId(@Param("id") Long compteId);	

}
