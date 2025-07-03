package teng.dev.expenseapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ExpenseService
{
	private final ExpenseRepository expenseRepository;
	private final ExpenseCategoryRepository expenseCategoryRepository;

	public List<ExpenseResponseDTO> getAllExpenses()
	{
		log.info("Fetching all expense expenses.");

		if (expenseRepository.count() == 0)
		{
			log.warn("No expense record found in the database");
			throw new ExpenseNotFoundException("No expense record found.");
		}

		final var expenses = expenseRepository.findAll().stream().map(DataMapper::mapToExpenseResponseDto).toList();

		log.info("Fetched {} expense records.", expenses.size());

		return expenses;
	}

	public ExpenseResponseDTO getExpenseById(Long id)
	{
		log.info("Fetching expense record with id={}", id);

		Expense expense = expenseRepository
				.findById(id)
				.orElseThrow(() ->
						{
							log.error("Expense record with id={} not found", id);
							return new ExpenseNotFoundException(
									String.format("Expense record with id=%s does not exist.", id)
							);
						}
				);
		log.info("Expense found: {}", expense);

		return DataMapper.mapToExpenseResponseDto(expense);
	}

	public ExpenseResponseDTO addExpense(ExpenseRequestDTO request)
	{
		log.info("Attempting to add new expense: {}", request);

		Expense toSaveExpense = new Expense();
		toSaveExpense.setDescription(request.getDescription());
		toSaveExpense.setAmount(BigDecimal.valueOf(request.getAmount()));
		toSaveExpense.setTransactionDate(request.getTransactionDate());

		final var category = expenseCategoryRepository
				.findById(request.getCategoryId())
				.orElseThrow(() ->
						{
							log.error("Expense category record with id={} not found", request.getCategoryId());
							return new CategoryNotFoundException(String.format("Category with id=%s not found.",
									request.getCategoryId()));
						}
				);

		toSaveExpense.setCategory(category);

		Expense savedExpense = expenseRepository.saveAndFlush(toSaveExpense);

		log.info("Added expense record {}", savedExpense);
		return DataMapper.mapToExpenseResponseDto(savedExpense);
	}

	public void deleteExpenseById(Long id)
	{
		log.info("Attempting to delete expense record with id={}", id);

		expenseRepository.findById(id)
				.orElseThrow(() ->
						{
							log.error("Expense with id={} not found for deletion", id);
							return new ExpenseNotFoundException(
									String.format("Expense record with id=%s does not exist.", id));
						}
				);

		expenseRepository.deleteById(id);
		log.info("Deleted expense with id={}", id);
	}
}
