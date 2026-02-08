package fit.hutech.TruongGiaHuy.controllers.api;

import fit.hutech.TruongGiaHuy.entities.User;
import fit.hutech.TruongGiaHuy.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminCommonApiController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserInfoVm> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        List<String> roles = authentication.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .filter(r -> !r.startsWith("SCOPE_") && 
                             !r.startsWith("OIDC_") && 
                             !r.startsWith("FACTOR_") && 
                             !r.equals("OAUTH2_USER"))
                .collect(Collectors.toList());

        String displayName = authentication.getName();
        var userOpt = userService.getUserByPrincipal(authentication);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Use email as display name for OAuth users or if username is the ID
            if (user.getProvider() != null && !user.getProvider().equalsIgnoreCase("Local")) {
                displayName = user.getEmail();
            }
        }

        return ResponseEntity.ok(new UserInfoVm(displayName, roles));
    }

    public record UserInfoVm(String username, List<String> roles) {}
}


