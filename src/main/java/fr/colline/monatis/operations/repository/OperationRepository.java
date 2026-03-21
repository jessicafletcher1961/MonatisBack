package fr.colline.monatis.operations.repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.colline.monatis.operations.model.Operation;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

	public Optional<Operation> findById(Long id);

	public boolean existsById(Long id);
	
	public Optional<Operation> findByNumero(String numero);

	public boolean existsByNumero(String numero);

	public int countByCompteDepenseId(Long compteId);

	public int countByCompteRecetteId(Long compteId);

	public boolean existsByNumeroAndId(String numero, Long id);

	public boolean existsByNumeroAndIdNot(String numero, Long id);

	public List<Operation> findByCompteDepenseIdAndDateValeurBetween(
			Long compteId, 
			ZonedDateTime dateDebut,
			ZonedDateTime dateFin);

	public List<Operation> findByCompteRecetteIdAndDateValeurBetween(
			Long compteId, 
			ZonedDateTime dateDebut,
			ZonedDateTime dateFin);
}
