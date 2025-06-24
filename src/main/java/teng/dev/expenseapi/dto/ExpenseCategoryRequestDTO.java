package teng.dev.expenseapi.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseCategoryRequestDTO
{
	@NotNull
	private String name;
}
