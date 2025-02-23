package teng.dev.expenseapi.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.*;
import java.time.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseRequestDto
{
	@NotBlank(message = "Description is required")
	private String description;

	@DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
	private BigDecimal amount;

	private Long categoryId;

	@NotNull(message = "Date is required")
	private LocalDateTime date;
}
