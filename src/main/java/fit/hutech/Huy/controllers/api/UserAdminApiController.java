package fit.hutech.Huy.controllers.api;

import fit.hutech.Huy.entities.Role;
import fit.hutech.Huy.services.UserService;
import fit.hutech.Huy.viewmodels.RoleGetVm;
import fit.hutech.Huy.viewmodels.UserGetVm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class UserAdminApiController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserGetVm>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers().stream()
                .map(UserGetVm::from)
                .collect(Collectors.toList()));
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleGetVm>> getAllRoles() {
        return ResponseEntity.ok(userService.getAllRoles().stream()
                .map(RoleGetVm::from)
                .collect(Collectors.toList()));
    }

    @PostMapping("/{userId}/roles")
    public ResponseEntity<Void> updateUserRoles(@PathVariable Long userId, @RequestBody List<Long> roleIds) {
        userService.updateUserRoles(userId, roleIds);
        return ResponseEntity.ok().build();
    }
}
