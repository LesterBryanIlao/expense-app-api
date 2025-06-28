package teng.dev.expenseapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import teng.dev.expenseapi.dto.ExpenseCategoryRequestDTO;
import teng.dev.expenseapi.dto.ExpenseCategoryResponseDTO;
import teng.dev.expenseapi.entity.Expense;
import teng.dev.expenseapi.entity.ExpenseCategory;
import teng.dev.expenseapi.exception.CategoryNotFoundException;
import teng.dev.expenseapi.repository.ExpenseCategoryRepository;
import teng.dev.expenseapi.repository.ExpenseRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseCategoryServiceTest
{

	private final Long TEST_CATEGORY_ID = 99L;

	private final Long DEFAULT_CATEGORY_ID = 1L;

	private final String TEST_CATEGORY_NAME = "Test Category";

	private final String DEFAULT_CATEGORY_NAME = "Uncategorized";

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

		when(expenseCategoryRepository.findById(TEST_CATEGORY_ID)).thenReturn(Optional.empty());

		CategoryNotFoundException exception =
				assertThrows(
						CategoryNotFoundException.class,
						() -> expenseCategoryService.getCategoryById(TEST_CATEGORY_ID));

		assertEquals(
				String.format("Expense Category record with id=%d does not exist.", TEST_CATEGORY_ID),
				exception.getMessage());
	}

	@Test
	void getCategoryById_shouldReturnCategory()
	{
		ExpenseCategory category = new ExpenseCategory(TEST_CATEGORY_ID, "Uncategorized");

		when(expenseCategoryRepository.findById(TEST_CATEGORY_ID)).thenReturn(Optional.of(category));

		ExpenseCategoryResponseDTO result = expenseCategoryService.getCategoryById(TEST_CATEGORY_ID);

		assertEquals(category.getId(), result.getId());
		assertEquals(category.getName(), result.getName());
	}

	@Test
	void addCategory_whenNoRecord_shouldThrowException()
	{
		ExpenseCategoryRequestDTO request = new ExpenseCategoryRequestDTO(TEST_CATEGORY_NAME);

		ExpenseCategory category = new ExpenseCategory(TEST_CATEGORY_ID, TEST_CATEGORY_NAME);

		when(expenseCategoryRepository.findByName(request.getName())).thenReturn(Optional.of(category));

		CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class,
				() -> expenseCategoryService.addCategory(request));

		assertEquals(
				String.format(
						"Expense Category record with name='%s' already exist: %s.",
						request.getName(), category.getName()),
				exception.getMessage());
	}

	@Test
	void addCategory_shouldAddCategory()
	{
		ExpenseCategoryRequestDTO request = new ExpenseCategoryRequestDTO(TEST_CATEGORY_NAME);
		ExpenseCategory category = new ExpenseCategory(TEST_CATEGORY_ID, TEST_CATEGORY_NAME);

		when(expenseCategoryRepository.findByName(request.getName())).thenReturn(Optional.empty());
		when(expenseCategoryRepository.save(any(ExpenseCategory.class))).thenReturn(category);

		ExpenseCategoryResponseDTO result = expenseCategoryService.addCategory(request);

		assertEquals(TEST_CATEGORY_ID, result.getId());
		assertEquals(TEST_CATEGORY_NAME, result.getName());
	}

	@Test
	void deleteCategoryById_whenIdIsReserved_shouldThrowException()
	{
		RuntimeException exception = assertThrows(RuntimeException.class,
				() -> expenseCategoryService.deleteCategoryById(DEFAULT_CATEGORY_ID));

		assertEquals("Reserved id. Cannot delete.", exception.getMessage());
	}

	@Test
	void deleteCategoryById_whenCategoryDoesNotExist_shouldThrowException()
	{
		when(expenseCategoryRepository.findById(TEST_CATEGORY_ID)).thenReturn(Optional.empty());

		CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class,
				() -> expenseCategoryService.deleteCategoryById(TEST_CATEGORY_ID));

		assertEquals(
				String.format("Expense Category record with id=%d does not exist.", TEST_CATEGORY_ID),
				exception.getMessage());
	}

	@Test
	void deleteCategoryById_whenDefaultCategoryDoesNotExist_shouldThrowException()
	{
		when(expenseCategoryRepository.findById(TEST_CATEGORY_ID)).thenReturn(
				Optional.of(new ExpenseCategory(TEST_CATEGORY_ID, TEST_CATEGORY_NAME))
		);

		when(expenseRepository.findByCategoryId(TEST_CATEGORY_ID)).thenReturn(Collections.emptyList());

		when(expenseCategoryRepository.findById(DEFAULT_CATEGORY_ID)).thenReturn(Optional.empty());

		CategoryNotFoundException exception = assertThrows(CategoryNotFoundException.class,
				() -> expenseCategoryService.deleteCategoryById(TEST_CATEGORY_ID));

		assertEquals("Default category does not exist.", exception.getMessage());
	}

	@Test
	void deleteCategoryById_shouldReassignExpensesAndDeleteCategory() {
		// Arrange
		Long idToDelete = 2L;

		ExpenseCategory toDelete = new ExpenseCategory(idToDelete, "Travel");
		ExpenseCategory defaultCategory = new ExpenseCategory(1L, "Uncategorized");

		Expense expense1 = new Expense();
		expense1.setId(1L);
		expense1.setCategory(toDelete);

		Expense expense2 = new Expense();
		expense2.setId(2L);
		expense2.setCategory(toDelete);

		List<Expense> relatedExpenses = List.of(expense1, expense2);

		when(expenseCategoryRepository.findById(idToDelete)).thenReturn(Optional.of(toDelete));
		when(expenseCategoryRepository.findById(1L)).thenReturn(Optional.of(defaultCategory));
		when(expenseRepository.findByCategoryId(idToDelete)).thenReturn(relatedExpenses);

		// Act
		expenseCategoryService.deleteCategoryById(idToDelete);

		// Assert
		relatedExpenses.forEach(e ->
				assertEquals(defaultCategory, e.getCategory()));

		verify(expenseRepository).saveAllAndFlush(relatedExpenses);
		verify(expenseCategoryRepository).delete(toDelete);
	}
}
