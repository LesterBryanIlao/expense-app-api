package teng.dev.expenseapi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_user") // Explicitly defining table name
public class User
{
	@Id
	@GeneratedValue
	private Long id;

	private String firstName;

	private String lastName;

	private LocalDate dateOfBirth;
}
