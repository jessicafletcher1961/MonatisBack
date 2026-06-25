package fr.colline.monatis.budgets.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import fr.colline.monatis.budgets.model.Budget;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

	public Optional<Budget> findById(Long id);

	public boolean existsById(Long id);
	
	public Optional<Budget> findByCle(String cle);

	public boolean existsByCle(String cle);

	public Optional<Budget> findFirstByReferenceIdOrderByDateFinDesc(Long referenceId);

	public Optional<Budget> findByReferenceIdAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(Long referenceId, LocalDate dateCible1, LocalDate dateCible2);

	@Query(value = "select b from Budget b"
			+ " where (b.reference.id = :referenceId)"
			+ " and (b.dateDebut <= :dateFin and b.dateFin >= :dateDebut)")
	public List<Budget> findByReferenceIdAndDateRange(Long referenceId, LocalDate dateDebut, LocalDate dateFin);
	
	
}
