package fr.colline.monatis.references.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.colline.monatis.references.model.SousCategorie;

@Repository
public interface SousCategorieRepository extends ReferenceRepository<SousCategorie> {

	@Query(nativeQuery = true,
			value = "select count(1) from public.operation_detail where sous_categorie_id = :id")
	public int compterOperationDetailParSousCategorieId(@Param("id") Long sousCategorieId);

}
