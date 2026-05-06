package fr.colline.monatis.budgets.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.colline.monatis.budgets.model.Budget;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

	public Optional<Budget> findById(Long id);

	public boolean existsById(Long id);
	
	public Optional<Budget> findByCle(String cle);

	public boolean existsByCle(String cle);

	ArrayList<Budget> findByReferenceIdOrderByDateFinDesc(Long referenceId);

	boolean existsByReferenceId(Long referenceId);

	Optional<Budget> findByReferenceIdAndDateDebutLessThanEqualAndDateFinGreaterThanEqual(Long referenceId, LocalDate dateCible1, LocalDate dateCible2);

	boolean existsByReferenceIdAndDateDebutLessThanEqualAndDateFinGreaterThan(Long referenceId, LocalDate dateCible1, LocalDate dateCible2);

	Optional<Budget> findFirstByReferenceIdOrderByDateFinDesc(Long referenceId);
}
