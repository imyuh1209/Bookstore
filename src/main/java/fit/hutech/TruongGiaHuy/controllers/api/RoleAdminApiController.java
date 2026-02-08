package fit.hutech.TruongGiaHuy.controllers.api;

import fit.hutech.TruongGiaHuy.entities.Role;
import fit.hutech.TruongGiaHuy.services.RoleService;
import fit.hutech.TruongGiaHuy.viewmodels.PermissionGetVm;
import fit.hutech.TruongGiaHuy.viewmodels.RoleGetVm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class RoleAdminApiController {

    private final RoleService roleService;

    @GetMapping("/roles")
    public ResponseEntity<List<RoleGetVm>> getAllRoles() {
        return ResponseEntity.ok(roleService.getAllRoles().stream()
                .map(RoleGetVm::from)
                .collect(Collectors.toList()));
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<RoleGetVm> getRole(@PathVariable Long id) {
        return ResponseEntity.ok(RoleGetVm.from(roleService.getRoleById(id)));
    }

    @PostMapping("/roles")
    public ResponseEntity<RoleGetVm> createRole(@RequestBody Role role) {
        return ResponseEntity.ok(RoleGetVm.from(roleService.saveRole(role)));
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<RoleGetVm> updateRole(@PathVariable Long id, @RequestBody Role roleDetails) {
        Role role = roleService.getRoleById(id);
        role.setName(roleDetails.getName());
        role.setDescription(roleDetails.getDescription());
        return ResponseEntity.ok(RoleGetVm.from(roleService.saveRole(role)));
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/permissions")
    public ResponseEntity<List<PermissionGetVm>> getAllPermissions() {
        return ResponseEntity.ok(roleService.getAllPermissions().stream()
                .map(PermissionGetVm::from)
                .collect(Collectors.toList()));
    }

    @PostMapping("/roles/{roleId}/permissions")
    public ResponseEntity<Void> updateRolePermissions(@PathVariable Long roleId, @RequestBody List<Long> permissionIds) {
        roleService.updateRolePermissions(roleId, permissionIds);
        return ResponseEntity.ok().build();
    }
}


