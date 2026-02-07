package fit.hutech.Huy.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @GetMapping
    public String listOrders() {
        return "redirect:/admin/orders.html";
    }
}
