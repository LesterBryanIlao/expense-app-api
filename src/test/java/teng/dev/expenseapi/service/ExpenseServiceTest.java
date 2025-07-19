package teng.dev.expenseapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import teng.dev.expenseapi.dto.ExpenseRequestDTO;
import teng.dev.expenseapi.dto.ExpenseResponseDTO;
import teng.dev.expenseapi.entity.Expense;
import teng.dev.expenseapi.entity.ExpenseCategory;
import teng.dev.expenseapi.exception.CategoryNotFoundException;
import teng.dev.expenseapi.exception.ExpenseNotFoundException;
import teng.dev.expenseapi.repository.ExpenseCategoryRepository;
import teng.dev.expenseapi.repository.ExpenseRepository;
import teng.dev.expenseapi.util.DataMapper;
import teng.dev.expenseapi.util.StringConstants;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseServiceTest
{
	private final Long DEFAULT_CATEGORY_ID = 1L;

	private final Long TEST_ID = 99L;

	private final String TEST_CATEGORY_NAME = "Test Category";

	@Mock
	private ExpenseRepository expenseRepository;
	@Mock
	private ExpenseCategoryRepository expenseCategoryRepository;
	@InjectMocks
	private ExpenseService expenseService;

	@Test
	void getAllExpenses_whenNoRecord_shouldThrowException()
	{
		when(expenseRepository.count()).thenReturn(0L);

		final var exception = assertThrows(ExpenseNotFoundException.class, () -> expenseService.getAllExpenses());

		assertEquals("No expense record found.", exception.getMessage());
	}

	@Test
	void getAllExpenses_whenNotEmpty_shouldReturnList()
	{

		List<Expense> expenses = new ArrayList<>();
		Expense expense1 = addExpenseEntity(1L, "Test Expense 1", BigDecimal.valueOf(100));
		Expense expense2 = addExpenseEntity(2L, "Test Expense 2", BigDecimal.valueOf(200), 2L, "Test Category");

		expenses.add(expense1);
		expenses.add(expense2);

		when(expenseRepository.count()).thenReturn((long) expenses.size());
		when(expenseRepository.findAll()).thenReturn(expenses);

		List<ExpenseResponseDTO> result = expenseService.getAllExpenses();

		assertEquals(2, result.size());

		assertAll("First Expense",
				() -> assertEquals(expense1.getId(), result.get(0).getId()),
				() -> assertEquals(expense1.getDescription(), result.get(0).getDescription()),
				() -> assertEquals(expense1.getAmount(), result.get(0).getAmount()),
				() -> assertEquals(expense1.getCategory().getId(), result.get(0).getCategory().getId()),
				() -> assertEquals(expense1.getCategory().getName(), result.get(0).getCategory().getName()),
				() -> assertEquals(expense1.getTransactionDate(), result.get(0).getTransactionDate())
		);

		assertAll("Second Expense",
				() -> assertEquals(expense2.getId(), result.get(1).getId()),
				() -> assertEquals(expense2.getDescription(), result.get(1).getDescription()),
				() -> assertEquals(expense2.getAmount(), result.get(1).getAmount()),
				() -> assertEquals(expense2.getCategory().getId(), result.get(1).getCategory().getId()),
				() -> assertEquals(expense2.getCategory().getName(), result.get(1).getCategory().getName()),
				() -> assertEquals(expense2.getTransactionDate(), result.get(1).getTransactionDate())
		);
	}

	@Test
	void getExpenseById_whenExpenseRecordDoesNotExist_shouldThrowException()
	{

		when(expenseRepository.findById(TEST_ID)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ExpenseNotFoundException.class,
				() -> expenseService.getExpenseById(TEST_ID));

		assertEquals(String.format("Expense record with id=%s does not exist.", TEST_ID),
				exception.getMessage());
	}

	@Test
	void getExpenseById_whenExpenseRecordExist_shouldReturnRecord()
	{
		Expense expense = addExpenseEntity(TEST_ID, "Test Expense", BigDecimal.valueOf(100));

		when(expenseRepository.findById(TEST_ID)).thenReturn(Optional.of(expense));

		ExpenseResponseDTO result = expenseService.getExpenseById(TEST_ID);

		assertNotNull(result);
		assertEquals(expense.getId(), result.getId());
		assertEquals(expense.getDescription(), result.getDescription());
		assertEquals(expense.getAmount(), result.getAmount());
		assertEquals(expense.getCategory().getId(), result.getCategory().getId());
		assertEquals(expense.getCategory().getName(), result.getCategory().getName());
		assertEquals(expense.getTransactionDate(), result.getTransactionDate());

	}

	@Test
	void addExpense_whenCategoryDoesNotExist_shouldThrowException()
	{
		ExpenseRequestDTO mockRequest = mock(ExpenseRequestDTO.class);

		when(mockRequest.getCategoryId()).thenReturn(TEST_ID);
		when(mockRequest.getDescription()).thenReturn("Mock Description");
		when(mockRequest.getAmount()).thenReturn(100.0);
		when(mockRequest.getTransactionDate()).thenReturn(LocalDate.of(2025, 7, 1));

		when(expenseCategoryRepository.findById(TEST_ID)).thenReturn(Optional.empty());

		Exception exception = assertThrows(CategoryNotFoundException.class,
				() -> expenseService.addExpense(mockRequest));

		assertEquals(String.format("Category with id=%s not found.", TEST_ID),
				exception.getMessage());
	}

	@Test
	void addExpense_whenCategoryExist_shouldAddExpense()
	{
		ExpenseRequestDTO request = new ExpenseRequestDTO();
		request.setDescription("Lunch");
		request.setAmount(150.0);
		request.setTransactionDate(LocalDate.of(2025, 6, 30));
		request.setCategoryId(1L);

		ExpenseCategory category = new ExpenseCategory();
		category.setId(1L);
		category.setName("Food");

		Expense savedExpense = new Expense();
		savedExpense.setId(100L);
		savedExpense.setDescription("Lunch");
		savedExpense.setAmount(BigDecimal.valueOf(150.0));
		savedExpense.setTransactionDate(LocalDate.of(2025, 6, 30));
		savedExpense.setCategory(category);

		ExpenseResponseDTO expectedResponse = new ExpenseResponseDTO();
		expectedResponse.setId(100L);
		expectedResponse.setDescription("Lunch");
		expectedResponse.setAmount(BigDecimal.valueOf(150.0));
		expectedResponse.setTransactionDate(LocalDate.of(2025, 6, 30));
		expectedResponse.setCategory(DataMapper.mapToExpenseCategoryDto(category));

		when(expenseCategoryRepository.findById(1L)).thenReturn(Optional.of(category));
		when(expenseRepository.saveAndFlush(any(Expense.class))).thenReturn(savedExpense);

		ExpenseResponseDTO actualResponse = expenseService.addExpense(request);

		assertNotNull(actualResponse);
		assertEquals(expectedResponse.getId(), actualResponse.getId());
		assertEquals(expectedResponse.getAmount(), actualResponse.getAmount());
		assertEquals(expectedResponse.getDescription(), actualResponse.getDescription());
	}

	@Test
	void deleteExpenseById_whenExpenseRecordDoesNotExist_shouldThrowException()
	{
		when(expenseRepository.findById(TEST_ID)).thenReturn(Optional.empty());

		Exception exception = assertThrows(ExpenseNotFoundException.class, () ->  expenseService.deleteExpenseById(TEST_ID));

		assertEquals(String.format("Expense record with id=%s does not exist.", TEST_ID), exception.getMessage());
	}

	@Test
	void deleteExpenseById_whenExpenseRecordExist_shouldDeleteExpense()
	{
		Expense mockExpense = mock(Expense.class);
		when(expenseRepository.findById(TEST_ID)).thenReturn(Optional.of(mockExpense));

		expenseService.deleteExpenseById(TEST_ID);

		verify(expenseRepository).deleteById(TEST_ID);
	}

	private Expense addExpenseEntity(Long id, String name, BigDecimal amount)
	{
		return addExpenseEntity(
				id,
				name,
				amount,
				DEFAULT_CATEGORY_ID,
				StringConstants.UNCATEGORIZED
		);
	}

	private Expense addExpenseEntity(Long id, String name, BigDecimal amount, Long categoryId, String categoryName)
	{
		return new Expense(
				id,
				name,
				amount,
				new ExpenseCategory(categoryId, categoryName),
				LocalDate.now()
		);
	}

}
