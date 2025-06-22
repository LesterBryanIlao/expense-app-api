package teng.dev.expenseapi.dto;

import lombok.*;

import java.math.*;
import java.time.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseResponseDto
{
	private Long id;

	private String description;

	private BigDecimal amount;

	private ExpenseCategoryResponseDto category;

	private LocalDateTime date;
}
