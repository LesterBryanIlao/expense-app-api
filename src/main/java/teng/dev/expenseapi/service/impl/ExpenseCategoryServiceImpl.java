package teng.dev.expenseapi.service.impl;

import lombok.*;
import org.springframework.stereotype.*;
import teng.dev.expenseapi.dto.*;
import teng.dev.expenseapi.entity.*;
import teng.dev.expenseapi.repository.*;
import teng.dev.expenseapi.service.*;
import teng.dev.expenseapi.util.*;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ExpenseCategoryServiceImpl implements ExpenseCategoryService
{
	private final ExpenseCategoryRepository expenseCategoryRepository;

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
				.filter(category -> category.getId() == null || Objects.equals(category.getId(), id) )
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
		ExpenseCategory category = expenseCategoryRepository.findByName(cleanCategory);

		if (category != null)
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


}
