package teng.dev.expenseapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import teng.dev.expenseapi.entity.Role;

import java.time.LocalDate;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest
{
	private String firstName;

	private String lastName;

	private LocalDate dateOfBirth;

	@NotBlank(message = "Username is required.")
	private String username;

	@NotBlank(message = "Password is required.")
	@Size(min = 5, message = "Password must be at least 5 characters.")
	private String password;

	private Role role;
}
