package fr.colline.monatis.operations.repository;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.colline.monatis.operations.model.Operation;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

	public Optional<Operation> findById(Long id);

	public boolean existsById(Long id);
	
	public Optional<Operation> findByNumero(String numero);

	public boolean existsByNumero(String numero);

	public Stream<Operation> findByCompteDepenseIdOrCompteRecetteIdOrderByDateValeur(
			Long compteDepenseId, 
			Long compteRecetteId);
}
