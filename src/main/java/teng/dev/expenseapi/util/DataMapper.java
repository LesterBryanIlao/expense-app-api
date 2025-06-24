package teng.dev.expenseapi.util;

import teng.dev.expenseapi.dto.*;
import teng.dev.expenseapi.entity.*;

public class DataMapper
{
	public static ExpenseResponseDTO mapToExpenseResponseDto(Expense expense)
	{
		if (expense==null){
			return null;
		}

		return ExpenseResponseDTO
				.builder()
				.id(expense.getId())
				.description(expense.getDescription())
				.amount(expense.getAmount())
				.category(mapToExpenseCategoryDto(expense.getCategory()))
				.transactionDate(expense.getTransactionDate()).build();
	}

	public static ExpenseCategoryResponseDTO mapToExpenseCategoryDto(ExpenseCategory expenseCategory)
	{
		if (expenseCategory==null){
			return null;
		}

		return teng.dev.expenseapi.dto.ExpenseCategoryResponseDTO
				.builder()
				.id(expenseCategory.getId())
				.name(expenseCategory.getName())
				.build();
	}
}
