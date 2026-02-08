package fit.hutech.TruongGiaHuy.controllers;

import fit.hutech.TruongGiaHuy.entities.User;
import fit.hutech.TruongGiaHuy.services.CartService;
import fit.hutech.TruongGiaHuy.services.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;
    private final UserService userService;

    @GetMapping
    public String showCart(HttpSession session, Model model) {
        model.addAttribute("cart", cartService.getCart(session));
        model.addAttribute("totalPrice", cartService.getSumPrice(session));
        model.addAttribute("totalQuantity", cartService.getSumQuantity(session));
        return "book/cart";
    }

    @GetMapping("/add/{id}")
    public String addToCart(HttpSession session, @PathVariable Long id) {
        cartService.addToCart(session, id, 1);
        return "redirect:/books";
    }

    @GetMapping("/remove/{id}")
    public String removeFromCart(HttpSession session, @PathVariable Long id) {
        cartService.removeFromCart(session, id);
        return "redirect:/cart";
    }

    @PostMapping("/update")
    public String updateCart(HttpSession session, @RequestParam Long id, @RequestParam int quantity) {
        cartService.updateCart(session, id, quantity);
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(HttpSession session, Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }
        try {
            User user = userService.getUserByPrincipal(principal)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            cartService.saveCart(session, user);
            return "redirect:/cart/success";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/cart";
        }
    }

    @GetMapping("/success")
    public String success() {
        return "book/order_success";
    }
}


