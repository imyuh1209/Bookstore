package fit.hutech.TruongGiaHuy.validators;

import fit.hutech.TruongGiaHuy.entities.Category;
import fit.hutech.TruongGiaHuy.repositories.ICategoryRepository;
import fit.hutech.TruongGiaHuy.validators.annotations.ValidCategoryId;
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


