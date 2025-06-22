package teng.dev.expenseapi.util;

import teng.dev.expenseapi.dto.*;
import teng.dev.expenseapi.entity.*;

public class DataMapper
{
	public static ExpenseResponseDto mapToExpenseResponseDto(Expense expense)
	{
		if (expense==null){
			return null;
		}

		return ExpenseResponseDto
				.builder()
				.id(expense.getId())
				.description(expense.getDescription())
				.amount(expense.getAmount())
				.category(mapToExpenseCategoryDto(expense.getCategory()))
				.date(expense.getDate()).build();
	}

	public static ExpenseCategoryResponseDto mapToExpenseCategoryDto(ExpenseCategory expenseCategory)
	{
		if (expenseCategory==null){
			return null;
		}

		return ExpenseCategoryResponseDto
				.builder()
				.id(expenseCategory.getId())
				.name(expenseCategory.getName())
				.build();
	}
}
