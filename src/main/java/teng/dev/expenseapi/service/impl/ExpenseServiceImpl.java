package teng.dev.expenseapi.service.impl;

import lombok.*;
import org.springframework.stereotype.*;
import teng.dev.expenseapi.dto.*;
import teng.dev.expenseapi.entity.*;
import teng.dev.expenseapi.repository.*;
import teng.dev.expenseapi.service.*;
import teng.dev.expenseapi.util.*;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService
{
	private final ExpenseRepository expenseRepository;

	@Override
	public List<ExpenseResponseDto> getAllExpenses()
	{
		List<Expense> expenses = expenseRepository.findAll();

		if (expenses.isEmpty())
		{
			throw new RuntimeException("No expense record found.");
		}

		return expenses.stream().map(DataMapper::mapToExpenseResponseDto).toList();
	}

	@Override
	public ExpenseResponseDto getExpenseById(Long id)
	{
		Expense expense = expenseRepository.findById(id).orElseThrow(() ->
				new RuntimeException(
						String.format("Expense record with id=%s does not exist.")
				)
		);

		return DataMapper.mapToExpenseResponseDto(expense);
	}
}
