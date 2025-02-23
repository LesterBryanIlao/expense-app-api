package teng.dev.expenseapi.repository;

import org.springframework.data.jpa.repository.*;
import teng.dev.expenseapi.entity.*;

public interface ExpenseRepository extends JpaRepository<Expense, Long>
{
}
