package teng.dev.expenseapi.service;

import teng.dev.expenseapi.dto.*;

import java.util.*;

public interface ExpenseCategoryService
{
	List<ExpenseCategoryResponseDto> getAllCategories();

	ExpenseCategoryResponseDto getCategoryById(Long id);

	ExpenseCategoryResponseDto addCategory(ExpenseCategoryRequestDto categoryDto);

	List<ExpenseCategoryResponseDto> getCategory(Long id, String name);
}
