package teng.dev.expenseapi.repository;

import org.springframework.data.jpa.repository.*;
import teng.dev.expenseapi.entity.*;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long>
{
	ExpenseCategory findByName(String cleanCategory);
}
