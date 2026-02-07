package fit.hutech.Huy.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String dashboard(Authentication authentication) {
        if (authentication != null) {
            boolean isBookManage = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("BookManage") || a.getAuthority().equals("ADMIN"));
            boolean isUserManage = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("UserManage"));
            
            if (isBookManage) {
                return "redirect:/admin/books";
            } else if (isUserManage) {
                return "redirect:/admin/users";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/permissions")
    public String permissions() {
        return "redirect:/admin/permissions.html";
    }
}
