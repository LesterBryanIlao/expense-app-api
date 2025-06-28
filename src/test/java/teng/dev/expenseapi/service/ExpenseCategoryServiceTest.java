package teng.dev.expenseapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import teng.dev.expenseapi.entity.ExpenseCategory;
import teng.dev.expenseapi.repository.ExpenseCategoryRepository;
import teng.dev.expenseapi.repository.ExpenseRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExpenseCategoryServiceTest {

	@Mock
	private ExpenseCategoryRepository expenseCategoryRepository;

	@Mock
	private ExpenseRepository expenseRepository;

	@InjectMocks
	private ExpenseCategoryService expenseCategoryService;

  @Test
  void getAllCategories_whenNoRecord_shouldThrowException(){

  when(expenseCategoryRepository.count()).thenReturn(0L);

  assertThrows(RuntimeException.class, () -> expenseCategoryService.getAllCategories());
  }
}
