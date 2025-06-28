package teng.dev.expenseapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "expense_category")
public class ExpenseCategory
{
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "expense_category_seq")
	@SequenceGenerator(
			name = "expense_category_seq",
			sequenceName = "expense_category_seq",
			allocationSize = 1
	)
	private Long id;

	@NotBlank(message = "Category name cannot be empty")
	@Column(unique = true, nullable = false)
	private String name;
}
