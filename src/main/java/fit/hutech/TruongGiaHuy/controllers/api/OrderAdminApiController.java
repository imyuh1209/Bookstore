package fit.hutech.TruongGiaHuy.controllers.api;

import fit.hutech.TruongGiaHuy.constants.InvoiceStatus;
import fit.hutech.TruongGiaHuy.entities.Invoice;
import fit.hutech.TruongGiaHuy.services.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin/orders")
@RequiredArgsConstructor
public class OrderAdminApiController {

    private final InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<List<OrderVm>> getAllOrders() {
        List<Invoice> invoices = invoiceService.getAllInvoices();
        List<OrderVm> orders = invoices.stream()
                .map(OrderVm::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDetailVm> getOrderById(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        if (invoice == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(OrderDetailVm.fromEntity(invoice));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        try {
            String statusStr = payload.get("status");
            InvoiceStatus status = InvoiceStatus.valueOf(statusStr);
            invoiceService.updateStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id) {
        try {
            // Admin can cancel any order, not just PENDING
            // But we should probably use updateStatus(id, CANCELLED) instead.
            // However, reuse cancelInvoice logic if we want to enforce PENDING check?
            // User requirement: "thêm chức năng huỷ đơn hàng" for admin/manager implies they can force cancel.
            // Let's allow force cancel.
            invoiceService.updateStatus(id, InvoiceStatus.CANCELLED);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            invoiceService.deleteInvoice(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // View Models
    public record OrderVm(Long id, String username, String date, Double totalPrice, String status, String statusLabel) {
        public static OrderVm fromEntity(Invoice i) {
            String displayUser = "Unknown";
            if (i.getUser() != null) {
                displayUser = i.getUser().getUsername();
                // Use email for OAuth users to avoid showing raw ID
                if (i.getUser().getProvider() != null && !i.getUser().getProvider().equalsIgnoreCase("Local")) {
                    displayUser = i.getUser().getEmail();
                }

                // Append roles
                if (i.getUser().getRoles() != null && !i.getUser().getRoles().isEmpty()) {
                    String roles = i.getUser().getRoles().stream()
                            .map(fit.hutech.TruongGiaHuy.entities.Role::getName)
                            .collect(Collectors.joining(", "));
                    displayUser += " (" + roles + ")";
                }
            }
            
            return new OrderVm(
                    i.getId(),
                    displayUser,
                    i.getInvoiceDate() != null ? i.getInvoiceDate().toString() : "",
                    i.getTotalPrice(),
                    i.getStatus() != null ? i.getStatus().name() : "PENDING",
                    i.getStatus() != null ? i.getStatus().label : "Chờ xác nhận"
            );
        }
    }

    public record OrderDetailVm(Long id, String username, String date, Double totalPrice, String status, String statusLabel, List<OrderItemVm> items) {
        public static OrderDetailVm fromEntity(Invoice i) {
            List<OrderItemVm> items = i.getItemInvoices().stream()
                    .map(item -> new OrderItemVm(
                            item.getBook().getTitle(),
                            item.getQuantity(),
                            item.getPrice()
                    ))
                    .collect(Collectors.toList());
            
            String displayUser = "Unknown";
            if (i.getUser() != null) {
                displayUser = i.getUser().getUsername();
                if (i.getUser().getProvider() != null && !i.getUser().getProvider().equalsIgnoreCase("Local")) {
                    displayUser = i.getUser().getEmail();
                }

                // Append roles
                if (i.getUser().getRoles() != null && !i.getUser().getRoles().isEmpty()) {
                    String roles = i.getUser().getRoles().stream()
                            .map(fit.hutech.TruongGiaHuy.entities.Role::getName)
                            .collect(Collectors.joining(", "));
                    displayUser += " (" + roles + ")";
                }
            }
            
            return new OrderDetailVm(
                    i.getId(),
                    displayUser,
                    i.getInvoiceDate() != null ? i.getInvoiceDate().toString() : "",
                    i.getTotalPrice(),
                    i.getStatus() != null ? i.getStatus().name() : "PENDING",
                    i.getStatus() != null ? i.getStatus().label : "Chờ xác nhận",
                    items
            );
        }
    }

    public record OrderItemVm(String bookTitle, int quantity, Double price) {}
}


