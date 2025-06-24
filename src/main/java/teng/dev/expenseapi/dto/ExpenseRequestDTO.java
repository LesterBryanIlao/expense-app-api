package teng.dev.expenseapi.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.*;
import java.time.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseRequestDTO
{
	@NotBlank(message = "Description is required")
	private String description;

	@NotNull(message = "Amount is required")
	@DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
	@Digits(integer = 10, fraction = 2, message = "Invalid amount format")
	private BigDecimal amount;

	private Long categoryId;

	@NotNull(message = "Transaction date is required")
	private LocalDate transactionDate;
}
