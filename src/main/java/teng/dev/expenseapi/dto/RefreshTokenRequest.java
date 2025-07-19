package teng.dev.expenseapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RefreshTokenRequest
{
	private String refreshToken;
}
