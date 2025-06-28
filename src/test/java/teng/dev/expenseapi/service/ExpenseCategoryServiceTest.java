package teng.dev.expenseapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import teng.dev.expenseapi.dto.ExpenseCategoryRequestDTO;
import teng.dev.expenseapi.dto.ExpenseCategoryResponseDTO;
import teng.dev.expenseapi.entity.ExpenseCategory;
import teng.dev.expenseapi.exception.CategoryNotFoundException;
import teng.dev.expenseapi.repository.ExpenseCategoryRepository;
import teng.dev.expenseapi.repository.ExpenseRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseCategoryServiceTest
{

	private final Long TEST_CATEOGRY_ID = 99L;

	private final String TEST_CATEGORY_NAME = "Test Category";

	@Mock
	private ExpenseCategoryRepository expenseCategoryRepository;

	@Mock
	private ExpenseRepository expenseRepository;

	@InjectMocks
	private ExpenseCategoryService expenseCategoryService;

	@Test
	void getAllCategories_whenNoRecord_shouldThrowException()
	{

		when(expenseCategoryRepository.count()).thenReturn(0L);

		assertThrows(RuntimeException.class, () -> expenseCategoryService.getAllCategories());
	}

	@Test
	void getAllCategories_shouldReturnRecords()
	{
		List<ExpenseCategory> expenseCategories =
				List.of(new ExpenseCategory(1L, "Uncategorized"), new ExpenseCategory(2L, "Food"));

		when(expenseCategoryRepository.count()).thenReturn((long) expenseCategories.size());
		when(expenseCategoryRepository.findAll()).thenReturn(expenseCategories);

		List<ExpenseCategoryResponseDTO> result = expenseCategoryService.getAllCategories();

		assertEquals(2, result.size());
		assertEquals("Uncategorized", result.get(0).getName());
		assertEquals("Food", result.get(1).getName());
	}

	@Test
	void getCategoryById_whenNoRecord_shouldThrowException()
	{

		when(expenseCategoryRepository.findById(TEST_CATEOGRY_ID)).thenReturn(Optional.empty());

		CategoryNotFoundException exception =
				assertThrows(
						CategoryNotFoundException.class,
						() -> expenseCategoryService.getCategoryById(TEST_CATEOGRY_ID));

		assertEquals(
				String.format("Expense Category record with id=%d does not exist.", TEST_CATEOGRY_ID),
				exception.getMessage());
	}

	@Test
	void getCategoryById_shouldReturnCategory()
	{
		ExpenseCategory category = new ExpenseCategory(TEST_CATEOGRY_ID, "Uncategorized");

		when(expenseCategoryRepository.findById(TEST_CATEOGRY_ID)).thenReturn(Optional.of(category));

		ExpenseCategoryResponseDTO result = expenseCategoryService.getCategoryById(TEST_CATEOGRY_ID);

		assertEquals(category.getId(), result.getId());
		assertEquals(category.getName(), result.getName());
	}

	@Test
	void addCategory_whenNoRecord_shouldThrowException()
	{
		ExpenseCategoryRequestDTO request = new ExpenseCategoryRequestDTO(TEST_CATEGORY_NAME);

		ExpenseCategory category = new ExpenseCategory(TEST_CATEOGRY_ID, "Uncategorized");

		when(expenseCategoryRepository.findByName(request.getName())).thenReturn(Optional.of(category));

		CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class,
				() -> expenseCategoryService.addCategory(request));

		assertEquals(
				String.format(
						"Expense Category record with name='%s' already exist: %s.",
						request.getName(), category.getName()),
				exception.getMessage());
	}
}
