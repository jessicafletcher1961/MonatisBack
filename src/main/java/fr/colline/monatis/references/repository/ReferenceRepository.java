package fr.colline.monatis.references.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.colline.monatis.references.model.Reference;

@Repository
public abstract interface ReferenceRepository<T extends Reference> extends JpaRepository<T, Long> {

	Optional<T> findById(Long referenceId);
	
	boolean existsById(Long referenceId);

	Optional<T> findByNom(String nom);

	boolean existsByNom(String nom);

}
