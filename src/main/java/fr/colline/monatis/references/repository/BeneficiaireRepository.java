package fr.colline.monatis.references.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.colline.monatis.references.model.Beneficiaire;

@Repository
public interface BeneficiaireRepository extends ReferenceRepository<Beneficiaire> {

	@Query(nativeQuery = true,
			value = "select count(1) from public.operation_detail_beneficiaire where beneficiaire_id = :id")
	public int compterOperationDetailParBeneficiaireId(@Param("id") Long beneficiaireId);	

}
