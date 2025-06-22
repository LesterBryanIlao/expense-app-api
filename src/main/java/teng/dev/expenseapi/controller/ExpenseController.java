package teng.dev.expenseapi.controller;

import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import teng.dev.expenseapi.dto.ExpenseResponseDto;
import teng.dev.expenseapi.service.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/expenses")
public class ExpenseController
{
	private final ExpenseService expenseService;

	@GetMapping
	public ResponseEntity<List<ExpenseResponseDto>> getAllExpenses()
	{
		return ResponseEntity.ok(expenseService.getAllExpenses());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ExpenseResponseDto> getExpenseById(@PathVariable Long id)
	{
		return ResponseEntity.ok(expenseService.getExpenseById(id));
	}
}
