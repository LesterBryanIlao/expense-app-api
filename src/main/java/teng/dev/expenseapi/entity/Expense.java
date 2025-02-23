package teng.dev.expenseapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "expense") // Explicitly defining table name
public class Expense {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false) // Ensuring it's required
	private String description;

	@DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
	@Digits(integer = 10, fraction = 2, message = "Amount format is invalid")
	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal amount;

	@ManyToOne
	@JoinColumn(name = "category_id", nullable = true) // Can be null
	private ExpenseCategory category;

	@Column(nullable = false)
	private LocalDateTime date;
}
