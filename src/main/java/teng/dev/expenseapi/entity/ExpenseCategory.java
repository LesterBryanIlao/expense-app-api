package teng.dev.expenseapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "expense_category")
public class ExpenseCategory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Category name cannot be empty")
	@Column(unique = true, nullable = false)
	private String name = "Uncategorized";

}
