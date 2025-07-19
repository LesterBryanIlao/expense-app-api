package teng.dev.expenseapi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import teng.dev.expenseapi.entity.ExpenseCategory;
import teng.dev.expenseapi.repository.ExpenseCategoryRepository;
import teng.dev.expenseapi.util.StringConstants;

@SpringBootApplication
public class ExpenseapiApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(ExpenseapiApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(ExpenseCategoryRepository expenseCategoryRepository) {
		return args -> {
			expenseCategoryRepository.findByName(StringConstants.UNCATEGORIZED)
					.orElseGet(() -> expenseCategoryRepository.saveAndFlush(
							new ExpenseCategory(null, StringConstants.UNCATEGORIZED)
					));
		};
	}

}
