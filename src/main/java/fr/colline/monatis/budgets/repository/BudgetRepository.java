package fr.colline.monatis.budgets.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.colline.monatis.budgets.model.Budget;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

	ArrayList<Budget> findByReferenceIdOrderByDateFinDesc(Long referenceId);

	boolean existsByReferenceId(Long referenceId);

	Optional<Budget> findByReferenceIdAndDateDebutLessThanEqualAndDateFinGreaterThan(Long referenceId, LocalDate dateCible1, LocalDate dateCible2);

	boolean existsByReferenceIdAndDateDebutLessThanEqualAndDateFinGreaterThan(Long referenceId, LocalDate dateCible1, LocalDate dateCible2);

	Optional<Budget> findFirstByReferenceIdOrderByDateFinDesc(Long referenceId);
}
