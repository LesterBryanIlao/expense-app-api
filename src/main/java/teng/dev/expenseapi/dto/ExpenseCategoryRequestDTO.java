package teng.dev.expenseapi.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseCategoryRequestDTO
{
	@NotBlank(message = "Category name cannot be empty")
	private String name;
}
