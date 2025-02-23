package teng.dev.expenseapi.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseCategoryResponseDto
{
	private Long id;
	private String name;
}
