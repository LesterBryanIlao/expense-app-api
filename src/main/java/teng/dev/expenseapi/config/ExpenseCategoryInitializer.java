package teng.dev.expenseapi.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import teng.dev.expenseapi.entity.ExpenseCategory;
import teng.dev.expenseapi.repository.ExpenseCategoryRepository;
import teng.dev.expenseapi.util.StringConstants;

//@Component
//@RequiredArgsConstructor
//public class ExpenseCategoryInitializer {
//
//	private final ExpenseCategoryRepository categoryRepository;

//	@PostConstruct
//	public void initDefaultCategory() {
//		categoryRepository.findByName(StringConstants.UNCATEGORIZED).orElseGet(() -> {
//			ExpenseCategory category = new ExpenseCategory();
//			category.setName("Uncategorized");
//			return categoryRepository.save(category);
//		});
//	}
//}
