package fr.colline.monatis.evaluations.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.colline.monatis.evaluations.model.Evaluation;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

	public Optional<Evaluation> findById(Long id);

	public boolean existsById(Long id);
	
	public Optional<Evaluation> findByCle(String cle);

	public boolean existsByCle(String cle);

	public List<Evaluation> findByCompteInterneIdOrderByDateSolde(Long id);

	public List<Evaluation> findByCompteInterneIdAndDateSoldeBetweenOrderByDateSolde(
			Long id, 
			LocalDate dateDebut,
			LocalDate dateFin);

	public Optional<Evaluation> findFirstByCompteInterneIdAndDateSoldeGreaterThanEqualOrderByDateSolde(
			Long id,
			LocalDate dateCible);

	public Optional<Evaluation> findFirstByCompteInterneIdAndDateSoldeLessThanEqualOrderByDateSoldeDesc(
			Long id, 
			LocalDate dateCible);

}
