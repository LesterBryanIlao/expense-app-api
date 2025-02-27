package teng.dev.expenseapi.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;
import teng.dev.expenseapi.entity.*;

import java.util.*;

public interface ExpenseRepository extends JpaRepository<Expense, Long>
{
	@Query("SELECT e FROM Expense e WHERE e.category.id = :id")
	List<Expense> findByCategoryId(@Param("id") Long id);
}
