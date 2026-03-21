package fr.colline.monatis.comptes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.colline.monatis.comptes.model.Compte;

@Repository
public interface CompteRepository<T extends Compte> extends JpaRepository<T, Long> {

	public Optional<T> findById(Long id);

	public boolean existsById(Long id);	
	
	public Optional<T> findByIdentifiant(String identifiant);

	public boolean existsByIdentifiant(String identifiant);

	@Query(nativeQuery = true,
			value = "select count(1) from operation where compte_depense_id = :id or compte_recette_id = :id")
	public int compterOperationByCompteId(@Param("id") Long compteId);	

}
