package teng.dev.expenseapi.service;

import org.springframework.stereotype.Service;
import teng.dev.expenseapi.dto.ExpenseCategoryRequestDTO;
import teng.dev.expenseapi.dto.ExpenseCategoryResponseDTO;
import teng.dev.expenseapi.entity.Expense;
import teng.dev.expenseapi.entity.ExpenseCategory;
import teng.dev.expenseapi.exception.CategoryNotFoundException;
import teng.dev.expenseapi.repository.ExpenseCategoryRepository;
import teng.dev.expenseapi.repository.ExpenseRepository;
import teng.dev.expenseapi.util.DataMapper;

import java.util.List;
import java.util.Optional;

@Service
public class ExpenseCategoryService
{
	private final ExpenseCategoryRepository expenseCategoryRepository;
	private final ExpenseRepository expenseRepository;

	public ExpenseCategoryService(
			ExpenseCategoryRepository expenseCategoryRepository, ExpenseRepository expenseRepository)
	{
		this.expenseCategoryRepository = expenseCategoryRepository;
		this.expenseRepository = expenseRepository;
	}

	public List<ExpenseCategoryResponseDTO> getAllCategories()
	{
		if (expenseCategoryRepository.count() == 0)
		{
			throw new RuntimeException("No Expense Category record found.");
		}

		return expenseCategoryRepository.findAll().stream()
				.map(DataMapper::mapToExpenseCategoryDto)
				.toList();
	}

	public ExpenseCategoryResponseDTO getCategoryById(Long id)
	{
		ExpenseCategory category =
				expenseCategoryRepository
						.findById(id)
						.orElseThrow(
								() ->
										new CategoryNotFoundException(
												String.format("Expense Category record with id=%d does not exist.",
                                                        id)));

		return DataMapper.mapToExpenseCategoryDto(category);
	}

	public ExpenseCategoryResponseDTO addCategory(ExpenseCategoryRequestDTO toAddCategory)
	{

		Optional<ExpenseCategory> existingCategory = expenseCategoryRepository.findByName(toAddCategory.getName());

		if (existingCategory.isPresent())
		{
			throw new CategoryNotFoundException(
					String.format(
							"Expense Category record with name='%s' already exist: %s.",
							toAddCategory.getName(), existingCategory.get().getName()));
		}

		ExpenseCategory savedCategory =
				expenseCategoryRepository.save(new ExpenseCategory(null, toAddCategory.getName()));

		return DataMapper.mapToExpenseCategoryDto(savedCategory);
	}

	public void deleteCategoryById(Long id)
	{
		if (id == 1)
		{
			throw new RuntimeException("Reserved id. Cannot delete.");
		}

		ExpenseCategory categoryToDelete =
				expenseCategoryRepository
						.findById(id)
						.orElseThrow(
								() ->
										new RuntimeException(
												String.format("Expense Category record with id=%d does not exist.",
                                                        id)));

		List<Expense> relatedExpenses = expenseRepository.findByCategoryId(id);

		ExpenseCategory defaultCategory =
				expenseCategoryRepository
						.findById(1L)
						.orElseThrow(
								() ->
										new CategoryNotFoundException(
												String.format("Expense Category record with id=%d does not exist.",
                                                        id)));

		relatedExpenses.forEach(relatedExpense -> relatedExpense.setCategory(defaultCategory));

		expenseRepository.saveAllAndFlush(relatedExpenses);

		expenseCategoryRepository.delete(categoryToDelete);
	}

	public ExpenseCategoryResponseDTO updateCategory(
			Long id, ExpenseCategoryRequestDTO request)
	{

		ExpenseCategory toUpdate =
				expenseCategoryRepository
						.findById(id)
						.orElseThrow(
								() ->
										new CategoryNotFoundException(
												String.format("Expense Category record with id=%d does not exist.",
                                                        id)));

		if (request.getName().equals(toUpdate.getName()))
		{
			throw new RuntimeException("No changes detected.");
		}
		toUpdate.setName(request.getName());

		ExpenseCategory updatedCategory = expenseCategoryRepository.save(toUpdate);

		return DataMapper.mapToExpenseCategoryDto(updatedCategory);
	}
}
