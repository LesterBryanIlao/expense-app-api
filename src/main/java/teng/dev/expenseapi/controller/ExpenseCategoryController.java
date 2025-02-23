package teng.dev.expenseapi.controller;

import jakarta.validation.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import teng.dev.expenseapi.dto.*;
import teng.dev.expenseapi.service.*;

import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class ExpenseCategoryController
{
	private final ExpenseCategoryService expenseCategoryService;

	@GetMapping
	public ResponseEntity<List<ExpenseCategoryResponseDto>> getAllCategories()
	{
		List<ExpenseCategoryResponseDto> response = expenseCategoryService.getAllCategories();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ExpenseCategoryResponseDto> getCategoryById(@PathVariable Long id)
	{
		ExpenseCategoryResponseDto response = expenseCategoryService.getCategoryById(id);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/search")
	public ResponseEntity<List<ExpenseCategoryResponseDto>> getCategory(@RequestParam(value = "id", required = false) Long id,
	                                                              @RequestParam(value = "name", required = false) String name){

		List<ExpenseCategoryResponseDto> response = expenseCategoryService.getCategory(id, name);

		return ResponseEntity.ok(response);
	}
	@PostMapping
	public ResponseEntity<ExpenseCategoryResponseDto> addCategory(@RequestBody @Valid ExpenseCategoryRequestDto categoryDto)
	{
		ExpenseCategoryResponseDto response = expenseCategoryService.addCategory(categoryDto);
		return ResponseEntity.ok(response);
	}
}
