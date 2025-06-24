package teng.dev.expenseapi.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpenseCategoryResponseDTO
{
	private Long id;
	private String name;
}
