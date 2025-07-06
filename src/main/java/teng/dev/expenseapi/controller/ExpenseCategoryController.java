package teng.dev.expenseapi.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import teng.dev.expenseapi.dto.ExpenseCategoryRequestDTO;
import teng.dev.expenseapi.dto.ExpenseCategoryResponseDTO;
import teng.dev.expenseapi.service.ExpenseCategoryService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class ExpenseCategoryController {
    private final ExpenseCategoryService expenseCategoryService;

    @GetMapping
    public ResponseEntity<List<ExpenseCategoryResponseDTO>> getAllCategories() {
        List<ExpenseCategoryResponseDTO> response = expenseCategoryService.getAllCategories();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExpenseCategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        ExpenseCategoryResponseDTO response = expenseCategoryService.getCategoryById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
     public ResponseEntity<ExpenseCategoryResponseDTO> addCategory(
            @RequestBody @Valid ExpenseCategoryRequestDTO request) {
        ExpenseCategoryResponseDTO response = expenseCategoryService.addCategory(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ExpenseCategoryResponseDTO> updateCategory(
            @PathVariable Long id, @RequestBody @Valid ExpenseCategoryRequestDTO request) {
      ExpenseCategoryResponseDTO response = expenseCategoryService.updateCategory(id, request);
      return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {
        expenseCategoryService.deleteCategoryById(id);
    }
}
