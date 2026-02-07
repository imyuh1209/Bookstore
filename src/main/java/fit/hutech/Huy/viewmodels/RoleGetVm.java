package fit.hutech.Huy.viewmodels;

import fit.hutech.Huy.entities.Role;
import java.util.List;
import java.util.stream.Collectors;

public record RoleGetVm(Long id, String name, String description, List<PermissionGetVm> permissions) {
    public static RoleGetVm from(Role role) {
        List<PermissionGetVm> permissionVms = role.getPermissions() != null 
            ? role.getPermissions().stream().map(PermissionGetVm::from).collect(Collectors.toList())
            : List.of();
        return new RoleGetVm(role.getId(), role.getName(), role.getDescription(), permissionVms);
    }
}
