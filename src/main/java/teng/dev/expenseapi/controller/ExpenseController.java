package teng.dev.expenseapi.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import teng.dev.expenseapi.dto.ExpenseRequestDTO;
import teng.dev.expenseapi.dto.ExpenseResponseDTO;
import teng.dev.expenseapi.service.ExpenseService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/expenses")
public class ExpenseController
{
	private final ExpenseService expenseService;

	@GetMapping
	public ResponseEntity<List<ExpenseResponseDTO>> getAllExpenses()
	{
		return ResponseEntity.ok(expenseService.getAllExpenses());
	}

	@GetMapping("/{id}")
	public ResponseEntity<ExpenseResponseDTO> getExpenseById(@PathVariable Long id)
	{
		return ResponseEntity.ok(expenseService.getExpenseById(id));
	}

	@PostMapping
	public ResponseEntity<ExpenseResponseDTO> createExpense(@RequestBody ExpenseRequestDTO request)
	{
		return ResponseEntity.ok(expenseService.createExpense(request));
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteExpenseById(@PathVariable Long id)
	{
		expenseService.deleteExpenseById(id);
	}

}
