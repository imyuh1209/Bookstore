package fit.hutech.Huy.validators;

import fit.hutech.Huy.entities.Category;
import fit.hutech.Huy.repositories.ICategoryRepository;
import fit.hutech.Huy.validators.annotations.ValidCategoryId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ValidCategoryIdValidator implements ConstraintValidator<ValidCategoryId, Category> {
    
    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public boolean isValid(Category category, ConstraintValidatorContext context) {
        return category != null && category.getId() != null && categoryRepository.existsById(category.getId());
    }
}
