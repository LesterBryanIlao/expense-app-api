package teng.dev.expenseapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import teng.dev.expenseapi.dto.ExpenseRequestDTO;
import teng.dev.expenseapi.dto.ExpenseResponseDTO;
import teng.dev.expenseapi.entity.Expense;
import teng.dev.expenseapi.entity.ExpenseCategory;
import teng.dev.expenseapi.exception.ExpenseNotFoundException;
import teng.dev.expenseapi.repository.ExpenseCategoryRepository;
import teng.dev.expenseapi.repository.ExpenseRepository;
import teng.dev.expenseapi.util.DataMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService
{
	private final ExpenseRepository expenseRepository;
	private final ExpenseCategoryRepository categoryRepository;

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

	public ExpenseResponseDTO createExpense(ExpenseRequestDTO request) {
		Expense toSaveExpense = new Expense();
		toSaveExpense.setDescription(request.getDescription());
		toSaveExpense.setAmount(request.getAmount());
		toSaveExpense.setTransactionDate(request.getTransactionDate());
		if (request.getCategoryId() == null) {
			ExpenseCategory defaultCategory = categoryRepository.findById(1L)
					.orElseThrow(() -> new RuntimeException("Default category not found"));
			toSaveExpense.setCategory(defaultCategory);
		}
		Expense savedExpense = expenseRepository.save(toSaveExpense);

		return DataMapper.mapToExpenseResponseDto(savedExpense);
	}

	public Expense createExpenseFromDTO(ExpenseRequestDTO dto) {
		Expense expense = new Expense();
		expense.setDescription(dto.getDescription());
		expense.setAmount(dto.getAmount());
		expense.setTransactionDate(dto.getTransactionDate());

		ExpenseCategory category = (dto.getCategoryId() != null)
				? categoryRepository.findById(dto.getCategoryId())
				.orElseThrow(() -> new RuntimeException("Category not found"))
				: categoryRepository.findById(1L)
				.orElseThrow(() -> new RuntimeException("Default category not found"));

		expense.setCategory(category);
		return expenseRepository.save(expense);
	}
}
