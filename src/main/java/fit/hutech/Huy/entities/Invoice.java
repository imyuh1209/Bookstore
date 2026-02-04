package fit.hutech.Huy.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_date")
    private Date invoiceDate;

    @Column(name = "total_price")
    private Double totalPrice;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<ItemInvoice> itemInvoices;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
