package fr.colline.monatis.operations.repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fr.colline.monatis.operations.model.Operation;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

	public Optional<Operation> findById(Long id);

	public boolean existsById(Long id);
	
	public Optional<Operation> findByNumero(String numero);

	public boolean existsByNumero(String numero);

	@Query(value = "select o from Operation o"
			+ " where (:dateDebut is null or o.dateValeur >= :dateDebut)"
			+ " and (:dateFin is null or o.dateValeur <= :dateFin)"
			+ " order by o.dateValeur desc")
	public Stream<Operation> findByDateRange(LocalDate dateDebut, LocalDate dateFin);

	@Query(value = "select o from Operation o"
			+ " where (o.compteDepense.id = :compteId or o.compteRecette.id = :compteId)"
			+ " and (:dateDebut is null or o.dateValeur >= :dateDebut)"
			+ " and (:dateFin is null or o.dateValeur <= :dateFin)"
			+ " order by o.dateValeur desc")
	public Stream<Operation> findByCompteIdAndDateRange(Long compteId, LocalDate dateDebut, LocalDate dateFin);
}
