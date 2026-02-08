package fit.hutech.TruongGiaHuy.entities;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserValidationTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testUserValidation() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        // phone is null

        Set<jakarta.validation.ConstraintViolation<User>> violations = validator.validate(user);
        
        for (jakarta.validation.ConstraintViolation<User> violation : violations) {
            System.out.println("Violation: " + violation.getMessage());
        }

        assertTrue(violations.isEmpty(), "User should be valid with null phone");
    }
}


