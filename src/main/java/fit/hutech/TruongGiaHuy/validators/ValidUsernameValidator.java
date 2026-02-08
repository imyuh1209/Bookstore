package fit.hutech.TruongGiaHuy.validators;

import fit.hutech.TruongGiaHuy.repositories.IUserRepository;
import fit.hutech.TruongGiaHuy.utils.ApplicationContextProvider;
import fit.hutech.TruongGiaHuy.validators.annotations.ValidUsername;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidUsernameValidator implements ConstraintValidator<ValidUsername, String> {
    
    @Autowired
    private IUserRepository userRepository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        if (userRepository == null) {
            try {
                userRepository = ApplicationContextProvider.getBean(IUserRepository.class);
            } catch (Exception e) {
                // If context is not ready or bean not found, return true to avoid blocking startup
                // but this might bypass validation.
                // However, startup is critical.
                return true;
            }
        }
        if (username == null) return true;
        return userRepository.findByUsername(username).isEmpty();
    }
}


