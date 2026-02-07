package fit.hutech.Huy.controllers;

import fit.hutech.Huy.constants.InvoiceStatus;
import fit.hutech.Huy.entities.Invoice;
import fit.hutech.Huy.entities.User;
import fit.hutech.Huy.services.InvoiceService;
import fit.hutech.Huy.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final UserService userService;
    private final InvoiceService invoiceService;

    @GetMapping
    public String listOrders(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        User user = userService.findByUsername(username).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
        
        List<Invoice> orders = user.getInvoices().stream()
                .sorted(Comparator.comparing(Invoice::getId).reversed())
                .collect(Collectors.toList());
        model.addAttribute("orders", orders);
        return "user/orders";
    }

    @GetMapping("/{id}")
    public String orderDetail(@PathVariable Long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        User user = userService.findByUsername(username).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
        
        // Find invoice and ensure it belongs to user
        Invoice invoice = user.getInvoices().stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElse(null);
                
        if (invoice == null) {
            return "redirect:/orders";
        }
        
        model.addAttribute("invoice", invoice);
        return "user/order-detail";
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        if (principal == null) {
            return "redirect:/login";
        }
        String username = principal.getName();
        User user = userService.findByUsername(username).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }

        // Verify ownership
        Invoice invoice = user.getInvoices().stream()
                .filter(i -> i.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (invoice == null) {
            return "redirect:/orders";
        }

        try {
            invoiceService.cancelInvoice(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã huỷ đơn hàng thành công!");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể huỷ đơn hàng này!");
        }

        return "redirect:/orders/" + id;
    }
}
