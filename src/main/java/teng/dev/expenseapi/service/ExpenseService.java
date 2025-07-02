package teng.dev.expenseapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teng.dev.expenseapi.dto.ExpenseRequestDTO;
import teng.dev.expenseapi.dto.ExpenseResponseDTO;
import teng.dev.expenseapi.entity.Expense;
import teng.dev.expenseapi.exception.CategoryNotFoundException;
import teng.dev.expenseapi.exception.ExpenseNotFoundException;
import teng.dev.expenseapi.repository.ExpenseCategoryRepository;
import teng.dev.expenseapi.repository.ExpenseRepository;
import teng.dev.expenseapi.util.DataMapper;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService
{
	private final ExpenseRepository expenseRepository;
	private final ExpenseCategoryRepository expenseCategoryRepository;

	public List<ExpenseResponseDTO> getAllExpenses()
	{
		List<Expense> expenses = expenseRepository.findAll();

		if (expenses.isEmpty())
		{
			throw new ExpenseNotFoundException("No expense record found.");
		}

		return expenses.stream().map(DataMapper::mapToExpenseResponseDto).toList();
	}

	public ExpenseResponseDTO getExpenseById(Long id)
	{
		Expense expense = expenseRepository.findById(id).orElseThrow(() ->
				new ExpenseNotFoundException(
						String.format("Expense record with id=%s does not exist.", id)
				)
		);

		return DataMapper.mapToExpenseResponseDto(expense);
	}

	public ExpenseResponseDTO createExpense(ExpenseRequestDTO request)
	{
		Expense toSaveExpense = new Expense();
		toSaveExpense.setDescription(request.getDescription());
		toSaveExpense.setAmount(BigDecimal.valueOf(request.getAmount()));
		toSaveExpense.setTransactionDate(request.getTransactionDate());

		final var category = expenseCategoryRepository.findById(request.getCategoryId()).orElseThrow(() ->
				new CategoryNotFoundException("Category with id=" + request.getCategoryId() + " not found.")
		);

		toSaveExpense.setCategory(category);

		Expense savedExpense = expenseRepository.saveAndFlush(toSaveExpense);

		return DataMapper.mapToExpenseResponseDto(savedExpense);
	}

	public void deleteExpenseById(Long id)
	{
		expenseRepository.findById(id).orElseThrow(() -> new ExpenseNotFoundException(
				String.format("Expense record with id=%s does not exist.", id))
		);
		expenseRepository.deleteById(id);
	}
}
