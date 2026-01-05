package fr.colline.monatis.operations.repository;

import java.time.LocalDate;
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

	public List<Operation> findByCompteDepenseIdAndDateValeurBetweenOrderByDateValeur(
			Long compteId, 
			LocalDate dateDebut,
			LocalDate dateFin);

	public List<Operation> findByCompteDepenseIdAndDateValeurLessThanEqualOrderByDateValeur(
			Long compteId, 
			LocalDate dateFin);

	public List<Operation> findByCompteDepenseIdAndDateValeurGreaterThanEqualOrderByDateValeur(
			Long compteId,
			LocalDate dateDebut);

	public List<Operation> findByCompteRecetteIdAndDateValeurBetweenOrderByDateValeur(
			Long compteId, 
			LocalDate dateDebut,
			LocalDate dateFin);

	public List<Operation> findByCompteRecetteIdAndDateValeurLessThanEqualOrderByDateValeur(
			Long compteId, 
			LocalDate dateFin);

	public List<Operation> findByCompteRecetteIdAndDateValeurGreaterThanEqualOrderByDateValeur(
			Long compteId,
			LocalDate dateDebut);

	public List<Operation> findByCompteDepenseIdOrCompteRecetteIdOrderByDateValeur(
			Long compteId1, 
			Long compteId2);

	public List<Operation> findByCompteDepenseIdOrCompteRecetteIdAndDateValeurBetweenOrderByDateValeur(
			Long compteId1,
			Long compteId2, 
			LocalDate dateDebut, 
			LocalDate dateFin);

	public List<Operation> findByCompteDepenseIdOrCompteRecetteIdAndDateValeurLessThanEqualOrderByDateValeur(
			Long compteId1,
			Long compteId2,
			LocalDate dateFin);
	
	public List<Operation> findByCompteDepenseIdOrCompteRecetteIdAndDateValeurGreaterThanEqualOrderByDateValeur(
			Long compteId1,
			Long compteId2,
			LocalDate dateDebut);
}
