package fr.colline.monatis.references.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.colline.monatis.model.references.Reference;

@Repository 
public abstract interface ReferenceRepository <T extends Reference> extends JpaRepository <T, Long>{

	public Optional<T> findById(Long id);
	
	public boolean existsById(Long id);	
	
	public Optional<T> findByNom(String nom);
	
	public boolean existsByNom(String nom);

	public boolean existsByNomAndId(String referenceNom, Long referenceId);

	public boolean existsByNomAndIdNot(String referenceNom, Long referenceId);
}