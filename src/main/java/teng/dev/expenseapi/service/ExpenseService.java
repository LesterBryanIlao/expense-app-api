package teng.dev.expenseapi.service;

import teng.dev.expenseapi.dto.*;

import java.util.*;

public interface ExpenseService
{
	List<ExpenseResponseDto> getAllExpenses();

	ExpenseResponseDto getExpenseById(Long id);
}
