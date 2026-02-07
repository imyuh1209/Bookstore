package fit.hutech.Huy.controllers;

import fit.hutech.Huy.services.CartService;
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
            cartService.saveCart(session, principal.getName());
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
