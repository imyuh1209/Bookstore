package fit.hutech.Huy.services;

import fit.hutech.Huy.constants.Provider;
import fit.hutech.Huy.constants.Role;
import fit.hutech.Huy.entities.User;
import fit.hutech.Huy.repositories.IRoleRepository;
import fit.hutech.Huy.repositories.IUserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserService implements UserDetailsService {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public void save(@NotNull User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setProvider(Provider.LOCAL.value);
        userRepository.save(user);
    }
    
    public void setDefaultRole(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            roleRepository.findRoleByName("USER").ifPresent(role -> {
                user.getRoles().add(role);
                userRepository.save(user);
            });
        });
    }

    public User saveOauthUser(String email, String name, String providerId) {
        try {
            log.info("UserService: Attempting to save OAuth user - Email: {}, Name: {}, ProviderId: {}", email, name, providerId);
        
            String effectiveEmail = email;
        if (effectiveEmail == null || effectiveEmail.isEmpty()) {
            if (providerId != null) {
                effectiveEmail = providerId + "@google.placeholder.com";
                log.warn("UserService: Email is null. Generated placeholder email: {}", effectiveEmail);
            } else {
                log.error("UserService: Both Email and ProviderId are null! Cannot create user.");
                throw new IllegalArgumentException("Email and ProviderId cannot both be null");
            }
        }

        Optional<User> byEmail = userRepository.findByEmail(effectiveEmail);
        if (byEmail.isPresent()) {
            log.info("UserService: User found by email: {}", effectiveEmail);
            User user = byEmail.get();
            user.setProvider(Provider.GOOGLE.value);
            // Ensure roles exist
            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                fit.hutech.Huy.entities.Role role = roleRepository.findRoleByName("USER").orElseGet(() -> {
                    fit.hutech.Huy.entities.Role newRole = new fit.hutech.Huy.entities.Role();
                    newRole.setName("USER");
                    return roleRepository.save(newRole);
                });
                if (user.getRoles() == null) {
                    user.setRoles(new java.util.ArrayList<>());
                }
                user.getRoles().add(role);
            }
            return userRepository.saveAndFlush(user);
        }

        User user = new User();
        user.setEmail(effectiveEmail);
        user.setProvider(Provider.GOOGLE.value);
        user.setPhone("1234567890"); // Default phone for OAuth users
        
        String baseUsername = (email != null && email.contains("@")) ? email.split("@")[0] : (name != null ? name : "user" + providerId);
        baseUsername = baseUsername.replaceAll("[^a-zA-Z0-9]", "");
        if (baseUsername.isEmpty()) baseUsername = "user" + (providerId != null ? providerId : "000");
        
        String username = baseUsername;
        int count = 1;
        while (userRepository.findByUsername(username).isPresent()) {
            username = baseUsername + count++;
        }
        user.setUsername(username);
        log.info("UserService: Generated username: {}", username);
        
        user.setPassword(new BCryptPasswordEncoder().encode("OAUTH2_Generated_Password"));
        
        // Ensure roles exist
        fit.hutech.Huy.entities.Role role = roleRepository.findRoleByName("USER").orElseGet(() -> {
            fit.hutech.Huy.entities.Role newRole = new fit.hutech.Huy.entities.Role();
            newRole.setName("USER");
            return roleRepository.save(newRole);
        });
        user.setRoles(new java.util.ArrayList<>());
        user.getRoles().add(role);
        
        return userRepository.saveAndFlush(user);
        } catch (Exception e) {
            log.error("UserService: Error saving user to database: {}", e.getMessage(), e);
            throw e;
        }
    }
}
