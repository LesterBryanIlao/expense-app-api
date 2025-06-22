package teng.dev.expenseapi.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseCategoryRequestDto
{
	@NotNull
	private String name;
}
