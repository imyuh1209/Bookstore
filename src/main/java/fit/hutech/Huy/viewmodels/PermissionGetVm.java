package fit.hutech.Huy.viewmodels;

import fit.hutech.Huy.entities.Permission;

public record PermissionGetVm(Long id, String name, String description) {
    public static PermissionGetVm from(Permission permission) {
        return new PermissionGetVm(permission.getId(), permission.getName(), permission.getDescription());
    }
}
