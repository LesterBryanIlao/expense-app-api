package teng.dev.expenseapi.service.impl;

import org.springframework.stereotype.*;
import teng.dev.expenseapi.dto.*;
import teng.dev.expenseapi.entity.*;
import teng.dev.expenseapi.repository.*;
import teng.dev.expenseapi.service.*;
import teng.dev.expenseapi.util.*;

import java.util.*;


@Service
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService
{
	public final String DEFAULT_CATEGORY = "Uncategorized";
	private final ExpenseCategoryRepository expenseCategoryRepository;
	private final ExpenseRepository expenseRepository;

	public ExpenseCategoryServiceImpl(ExpenseCategoryRepository expenseCategoryRepository,
	                                  ExpenseRepository expenseRepository)
	{
		this.expenseCategoryRepository = expenseCategoryRepository;
		this.expenseRepository = expenseRepository;
	}

	@Override
	public List<ExpenseCategoryResponseDto> getAllCategories()
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

	@Override
	public ExpenseCategoryResponseDto getCategoryById(Long id)
	{
		ExpenseCategory category = expenseCategoryRepository.findById(id).orElseThrow(() ->
				new RuntimeException(
						String.format("Expense Category record with id=%d does not exist.", id))
		);

		return DataMapper.mapToExpenseCategoryDto(category);
	}

	@Override
	public List<ExpenseCategoryResponseDto> getCategory(Long id, String name)
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

	@Override
	public ExpenseCategoryResponseDto addCategory(ExpenseCategoryRequestDto toAddCategory)
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

	@Override
	public ExpenseCategoryResponseDto deleteCategoryById(Long id)
	{
		ExpenseCategory categoryToDelete = expenseCategoryRepository.findById(id).orElseThrow(() ->
				new RuntimeException(
						String.format("Expense Category record with id=%d does not exist.", id))
		);
		List<Expense> relatedExpenses = expenseRepository.findByCategoryId(id);

		relatedExpenses.forEach(this::updateCategory);

		expenseRepository.saveAll(relatedExpenses);

		expenseCategoryRepository.delete(categoryToDelete);

		return DataMapper.mapToExpenseCategoryDto(categoryToDelete);
	}

	private Expense updateCategory(Expense expense)
	{
		Optional<ExpenseCategory> defaultExpenseCategory = expenseCategoryRepository.findByName(DEFAULT_CATEGORY);

		if (defaultExpenseCategory.isEmpty())
		{
			defaultExpenseCategory = Optional.of(expenseCategoryRepository.save(new ExpenseCategory(null, "Uncategorized")));
		}

		expense.setCategory(defaultExpenseCategory.get());

		return expense;
	}
}
