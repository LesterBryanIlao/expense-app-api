package teng.dev.expenseapi.service;

import org.springframework.stereotype.Service;
import teng.dev.expenseapi.dto.ExpenseCategoryRequestDTO;
import teng.dev.expenseapi.dto.ExpenseCategoryResponseDTO;
import teng.dev.expenseapi.entity.Expense;
import teng.dev.expenseapi.entity.ExpenseCategory;
import teng.dev.expenseapi.repository.ExpenseCategoryRepository;
import teng.dev.expenseapi.repository.ExpenseRepository;
import teng.dev.expenseapi.util.DataMapper;
import teng.dev.expenseapi.util.StringConstants;
import teng.dev.expenseapi.util.StringUtil;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class ExpenseCategoryService
{
	public final ExpenseCategory DEFAULT_CATEGORY = new ExpenseCategory(1L, StringConstants.UNCATEGORIZED);
	private final ExpenseCategoryRepository expenseCategoryRepository;
	private final ExpenseRepository expenseRepository;

	public ExpenseCategoryService(ExpenseCategoryRepository expenseCategoryRepository,
	                              ExpenseRepository expenseRepository)
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

		return expenseCategoryRepository
				.findAll()
				.stream()
				.map(DataMapper::mapToExpenseCategoryDto)
				.toList();
	}

	public ExpenseCategoryResponseDTO getCategoryById(Long id)
	{
		ExpenseCategory category = expenseCategoryRepository.findById(id).orElseThrow(() ->
				new RuntimeException(
						String.format("Expense Category record with id=%d does not exist.", id))
		);

		return DataMapper.mapToExpenseCategoryDto(category);
	}

	public List<ExpenseCategoryResponseDTO> getCategory(Long id, String name)
	{
		List<ExpenseCategory> expenseCategory = expenseCategoryRepository.findAll()
				.stream()
				.filter(category -> category.getId() == null || Objects.equals(category.getId(), id))
				.filter(category -> category.getName().equals(StringUtil.capitalizeEachWord(name))).toList();

		if (expenseCategory.isEmpty())
		{
			throw new RuntimeException("No Expense Category record found.");
		}

		return expenseCategory.stream().map(DataMapper::mapToExpenseCategoryDto).toList();
	}

	public ExpenseCategoryResponseDTO addCategory(ExpenseCategoryRequestDTO toAddCategory)
	{
		String cleanCategory = StringUtil.capitalizeEachWord(toAddCategory.getName());
		Optional<ExpenseCategory> category = expenseCategoryRepository.findByName(cleanCategory);

		if (category.isPresent())
		{
			throw new RuntimeException(
					String.format("Expense Category record with name='%s' already exist: %s.",
							cleanCategory,
							category)
			);
		}

		ExpenseCategory savedCategory = expenseCategoryRepository.save(new ExpenseCategory(null, cleanCategory));

		return DataMapper.mapToExpenseCategoryDto(savedCategory);
	}

	public void deleteCategoryById(Long id)
	{
		if (id == 1)
		{
			throw new RuntimeException("Reserved id. Cannot delete.");
		}

		ExpenseCategory categoryToDelete = expenseCategoryRepository.findById(id).orElseThrow(() ->
				new RuntimeException(
						String.format("Expense Category record with id=%d does not exist.", id))
		);

		List<Expense> relatedExpenses = expenseRepository.findByCategoryId(id);

//		relatedExpenses.forEach(expense -> expense.setCategory(DEFAULT_CATEGORY));

		expenseRepository.saveAll(relatedExpenses);

		expenseCategoryRepository.delete(categoryToDelete);

	}
}
