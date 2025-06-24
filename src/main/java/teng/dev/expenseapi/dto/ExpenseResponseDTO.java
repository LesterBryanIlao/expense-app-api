package teng.dev.expenseapi.dto;

import lombok.*;

import java.math.*;
import java.time.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseResponseDTO
{
	private Long id;

	private String description;

	private BigDecimal amount;

	private ExpenseCategoryResponseDTO category;

	private LocalDate transactionDate;
}
