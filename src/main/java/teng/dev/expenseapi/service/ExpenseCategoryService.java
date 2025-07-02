package teng.dev.expenseapi.service;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class ExpenseCategoryService
{
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
		log.info("Fetching all expense categories");

		if (expenseCategoryRepository.count() == 0)
		{
			log.warn("No Expense Category record found in the database");
			throw new RuntimeException("No Expense Category record found.");
		}

		List<ExpenseCategoryResponseDTO> categories = expenseCategoryRepository.findAll().stream()
				.map(DataMapper::mapToExpenseCategoryDto)
				.toList();

		log.info("Fetched {} expense categories", categories.size());
		return categories;
	}

	public ExpenseCategoryResponseDTO getCategoryById(Long id)
	{
		log.info("Fetching expense category with id={}", id);

		ExpenseCategory category =
				expenseCategoryRepository
						.findById(id)
						.orElseThrow(() ->
						{
							log.error("Expense Category with id={} not found", id);
							return new CategoryNotFoundException(
									String.format("Expense Category record with id=%d does not exist.", id));
						});

		log.info("Expense category found: {}", category.getName());
		return DataMapper.mapToExpenseCategoryDto(category);
	}

	public ExpenseCategoryResponseDTO addCategory(ExpenseCategoryRequestDTO toAddCategory)
	{
		log.info("Attempting to add new expense category: {}", toAddCategory.getName());

		Optional<ExpenseCategory> existingCategory = expenseCategoryRepository.findByName(toAddCategory.getName());

		if (existingCategory.isPresent())
		{
			log.warn("Expense category '{}' already exists", toAddCategory.getName());
			throw new CategoryNotFoundException(
					String.format("Expense Category record with name='%s' already exist.", toAddCategory.getName()));
		}

		ExpenseCategory savedCategory = expenseCategoryRepository.save(new ExpenseCategory(null,
				toAddCategory.getName()));

		log.info("Expense category '{}' added successfully with ID={}", savedCategory.getName(),
				savedCategory.getId());

		return DataMapper.mapToExpenseCategoryDto(savedCategory);
	}

	public void deleteCategoryById(Long id)
	{
		log.info("Attempting to delete expense category with id={}", id);

		if (id == 1L)
		{
			log.warn("Attempted to delete reserved category id=1");
			throw new RuntimeException("Reserved Id. Cannot delete.");
		}


		ExpenseCategory categoryToDelete = expenseCategoryRepository.findById(id)
				.orElseThrow(() ->
				{
					log.error("Expense Category with id={} not found for deletion", id);
					return new CategoryNotFoundException(
							String.format("Expense Category record with id=%d does not exist.", id));
				});

		List<Expense> relatedExpenses = expenseRepository.findByCategoryId(id);
		log.info("Found {} expenses related to category id={}", relatedExpenses.size(), id);

		ExpenseCategory defaultCategory = expenseCategoryRepository.findById(1L)
				.orElseThrow(() ->
				{
					log.error("Default expense category with id=1 not found");
					return new CategoryNotFoundException("Default category does not exist.");
				});

		relatedExpenses.forEach(e -> e.setCategory(defaultCategory));
		expenseRepository.saveAllAndFlush(relatedExpenses);
		log.info("Reassigned all related expenses to default category");

		expenseCategoryRepository.delete(categoryToDelete);
		log.info("Deleted expense category with id={}", id);
	}

	public ExpenseCategoryResponseDTO updateCategory(Long id, ExpenseCategoryRequestDTO request)
	{
		log.info("Updating category with id={}", id);

		if (id == 1L)
		{
			log.warn("Attempted to update reserved category ID=1");
			throw new RuntimeException("Reserved Id. Cannot update.");
		}

		ExpenseCategory toUpdate = expenseCategoryRepository.findById(id)
				.orElseThrow(() ->
				{
					log.error("Expense Category with id={} not found for update", id);
					return new CategoryNotFoundException(
							String.format("Expense Category record with id=%d does not exist.", id));
				});

		if (request.getName().equals(toUpdate.getName()))
		{
			log.warn("No changes detected when updating category id={}", id);
			throw new RuntimeException("No changes detected.");
		}

		toUpdate.setName(request.getName());
		ExpenseCategory updatedCategory = expenseCategoryRepository.save(toUpdate);

		log.info("Updated category id={} with new name='{}'", updatedCategory.getId(), updatedCategory.getName());
		return DataMapper.mapToExpenseCategoryDto(updatedCategory);
	}
}
