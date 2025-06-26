package teng.dev.expenseapi.controller;

import jakarta.validation.*;
import lombok.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import teng.dev.expenseapi.dto.*;
import teng.dev.expenseapi.service.ExpenseCategoryService;

import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class ExpenseCategoryController
{
	private final ExpenseCategoryService expenseCategoryService;

	@GetMapping
	public ResponseEntity<List<ExpenseCategoryResponseDTO>> getAllCategories()
	{
		List<ExpenseCategoryResponseDTO> response = expenseCategoryService.getAllCategories();
		return ResponseEntity.ok(response);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ExpenseCategoryResponseDTO> getCategoryById(@PathVariable Long id)
	{
		ExpenseCategoryResponseDTO response = expenseCategoryService.getCategoryById(id);
		return ResponseEntity.ok(response);
	}

//	@GetMapping("/search")
//	public ResponseEntity<List<ExpenseCategoryResponseDTO>> getCategory(@RequestParam(value = "id", required = false) Long id,
//	                                                              @RequestParam(value = "name", required = false) String name){
//
//		List<ExpenseCategoryResponseDTO> response = expenseCategoryService.getCategory(id, name);
//
//		return ResponseEntity.ok(response);
//	}

	@PostMapping
	public ResponseEntity<ExpenseCategoryResponseDTO> addCategory(@RequestBody @Valid ExpenseCategoryRequestDTO categoryDto)
	{
		ExpenseCategoryResponseDTO response = expenseCategoryService.addCategory(categoryDto);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteCategory(@PathVariable Long id)
	{
		expenseCategoryService.deleteCategoryById(id);

	}
}
