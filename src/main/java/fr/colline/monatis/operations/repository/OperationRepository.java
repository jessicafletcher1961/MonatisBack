package fr.colline.monatis.operations.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import fr.colline.monatis.operations.model.Operation;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long>, JpaSpecificationExecutor<Operation> {
	
	public Optional<Operation> findByNumero(String numero);

	public boolean existsByNumero(String numero);
}
