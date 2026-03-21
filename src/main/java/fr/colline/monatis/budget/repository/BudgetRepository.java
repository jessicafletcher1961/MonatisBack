package fr.colline.monatis.budget.repository;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fr.colline.monatis.budget.model.Budget;

public abstract interface BudgetRepository<T extends Budget> extends JpaRepository<T, Long> {

	public Optional<T> findById(Long id);

	public boolean existsById(Long id);	

	@Query(nativeQuery = true,
			value = "select * from public.budget where reference_id = :referenceId order by date_fin desc")
	public ArrayList<T> findByReferenceIdOrderByDateFinDesc(Long referenceId);

	@Query(nativeQuery = true,
			value = "select 1 from public.budget where reference_id = :referenceId order by date_fin desc")
	public boolean existsByReferenceId(Long referenceId);

	@Query(nativeQuery = true,
			value = "select * from public.budget where reference_id = :referenceId and :date_cible between(date_debut, date_fin)")
	public Optional<T> findByReferenceIdAndDateCibleBetweenDateDebutAndDateFin(
			@Param("referenceId") Long referenceId, 
			@Param("dateCible") ZonedDateTime dateCible);

	@Query(nativeQuery = true,
			value = "select 1 from public.budget where reference_id = :referenceId and :date_cible between(date_debut, date_fin)")
	public boolean existsByReferenceIdAndDateCibleBetweenDateDebutAndDateFin(
			@Param("referenceId") Long referenceId, 
			@Param("dateCible") ZonedDateTime dateCible);
}
