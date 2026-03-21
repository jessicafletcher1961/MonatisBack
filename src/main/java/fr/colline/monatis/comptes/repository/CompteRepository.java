package fr.colline.monatis.comptes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.colline.monatis.comptes.model.Compte;

public abstract interface CompteRepository<T extends Compte> extends JpaRepository<T, Long> {

	public Optional<T> findById(Long id);

	public boolean existsById(Long id);	
	
	public Optional<T> findByIdentifiant(String identifiant);

	public boolean existsByIdentifiant(String identifiant);

	public boolean existsByIdentifiantAndId(String identifiant, Long id);

	public boolean existsByIdentifiantAndIdNot(String identifiant, Long id);

	@Query(nativeQuery = true,
			value = "select count(1) from operation where compte_depense_id = :compte_id or compte_recette_id = :compte_id")
	public int countOperationByCompteId(@Param("compte_id") Long compteId);	
}
