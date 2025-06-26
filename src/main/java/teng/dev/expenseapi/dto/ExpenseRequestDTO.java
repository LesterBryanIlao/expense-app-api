package teng.dev.expenseapi.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

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
	private Double amount;


	private Long categoryId = 1L;

	@NotNull(message = "Transaction date is required")
	private LocalDate transactionDate;
}
