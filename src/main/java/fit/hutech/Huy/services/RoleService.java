package fit.hutech.Huy.services;

import fit.hutech.Huy.entities.Permission;
import fit.hutech.Huy.entities.Role;
import fit.hutech.Huy.repositories.IPermissionRepository;
import fit.hutech.Huy.repositories.IRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {

    private final IRoleRepository roleRepository;
    private final IPermissionRepository permissionRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Role not found"));
    }

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    public void updateRolePermissions(Long roleId, List<Long> permissionIds) {
        Role role = getRoleById(roleId);
        List<Permission> permissions = permissionRepository.findAllById(permissionIds);
        role.setPermissions(permissions);
        roleRepository.save(role);
    }
}
