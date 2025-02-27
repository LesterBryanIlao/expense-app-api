package teng.dev.expenseapi.repository;

import org.springframework.data.jpa.repository.*;
import teng.dev.expenseapi.entity.*;

import java.util.*;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long>
{
	Optional<ExpenseCategory> findByName(String cleanCategory);
}
