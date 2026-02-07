package fit.hutech.Huy.controllers;

import fit.hutech.Huy.entities.Role;
import fit.hutech.Huy.entities.Permission;
import fit.hutech.Huy.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
public class AdminRoleController {

    private final RoleService roleService;

    @GetMapping
    public String listRoles() {
        return "redirect:/admin/roles.html";
    }

    // @PostMapping("/add")
    // public String addRole(@ModelAttribute Role role) {
    //     roleService.saveRole(role);
    //     return "redirect:/admin/roles";
    // }

    // @GetMapping("/delete/{id}")
    // public String deleteRole(@PathVariable Long id) {
    //     roleService.deleteRole(id);
    //     return "redirect:/admin/roles";
    // }

    // @PostMapping("/update-permissions")
    // public String updateRolePermissions(@RequestParam Long roleId, @RequestParam(required = false) List<Long> permissionIds) {
    //     if (permissionIds == null) {
    //         permissionIds = List.of();
    //     }
    //     roleService.updateRolePermissions(roleId, permissionIds);
    //     return "redirect:/admin/roles";
    // }
}
