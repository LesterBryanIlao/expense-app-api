package teng.dev.expenseapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseCategoryRequestDTO
{
	@NotBlank(message = "Category name cannot be empty")
	private String name;
}
