package fit.hutech.TruongGiaHuy.services;

import fit.hutech.TruongGiaHuy.entities.User;
import fit.hutech.TruongGiaHuy.repositories.IRoleRepository;
import fit.hutech.TruongGiaHuy.repositories.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private IUserRepository userRepository;
    private IRoleRepository roleRepository;
    private UserService userService;

    @BeforeEach
    void setup() {
        userRepository = Mockito.mock(IUserRepository.class);
        roleRepository = Mockito.mock(IRoleRepository.class);
        userService = new UserService(userRepository, roleRepository);
    }

    @Test
    void saveOauthUser_createsNewUser_whenEmailNotExist() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        // Generated username from email "test@example.com" -> "test"
        when(userRepository.findByUsername("test")).thenReturn(Optional.empty());
        
        fit.hutech.TruongGiaHuy.entities.Role role = new fit.hutech.TruongGiaHuy.entities.Role();
        role.setName("USER");
        when(roleRepository.findRoleByName("USER")).thenReturn(Optional.of(role));
        when(userRepository.saveAndFlush(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User result = userService.saveOauthUser("test@example.com", "Test User", "12345");

        assertEquals("test", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("Google", result.getProvider());
        assertEquals("1234567890", result.getPhone());
        verify(userRepository, times(1)).saveAndFlush(any(User.class));
    }

    @Test
    void saveOauthUser_updatesExistingByEmail_whenEmailExists() {
        User existing = new User();
        existing.setUsername("existingUser");
        existing.setEmail("test@example.com");
        existing.setRoles(new ArrayList<>());
        
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(existing));
        
        fit.hutech.TruongGiaHuy.entities.Role userRole = new fit.hutech.TruongGiaHuy.entities.Role();
        userRole.setName("USER");
        when(roleRepository.findRoleByName("USER")).thenReturn(Optional.of(userRole));
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(existing);

        User result = userService.saveOauthUser("test@example.com", "Any Name", "12345");

        assertEquals("Google", result.getProvider());
        verify(userRepository, times(1)).saveAndFlush(existing);
    }

    @Test
    void saveOauthUser_handlesUsernameConflict() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        // Derived username "test" exists
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(new User()));
        // "test1" does not exist
        when(userRepository.findByUsername("test1")).thenReturn(Optional.empty());
        
        fit.hutech.TruongGiaHuy.entities.Role role = new fit.hutech.TruongGiaHuy.entities.Role();
        role.setName("USER");
        when(roleRepository.findRoleByName("USER")).thenReturn(Optional.of(role));
        when(userRepository.saveAndFlush(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User result = userService.saveOauthUser("test@example.com", "Test User", "12345");

        assertEquals("test1", result.getUsername());
    }
}


