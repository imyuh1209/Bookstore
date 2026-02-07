package fit.hutech.Huy.controllers;

import fit.hutech.Huy.entities.Role;
import fit.hutech.Huy.entities.User;
import fit.hutech.Huy.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public String listUsers() {
        return "redirect:/admin/users.html";
    }

    // @PostMapping("/update-roles")
    // public String updateUserRoles(@RequestParam Long userId, @RequestParam(required = false) List<Long> roleIds) {
    //     if (roleIds == null) {
    //         roleIds = List.of(); // Empty list if no roles selected
    //     }
    //     userService.updateUserRoles(userId, roleIds);
    //     return "redirect:/admin/users";
    // }
}
