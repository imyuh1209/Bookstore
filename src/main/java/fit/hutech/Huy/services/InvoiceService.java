package fit.hutech.Huy.services;

import fit.hutech.Huy.constants.InvoiceStatus;
import fit.hutech.Huy.entities.Invoice;
import fit.hutech.Huy.repositories.IInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService {
    private final IInvoiceRepository invoiceRepository;

    public List<Invoice> getAllInvoices() {
        return invoiceRepository.findAll();
    }

    public Invoice getInvoiceById(Long id) {
        return invoiceRepository.findById(id).orElse(null);
    }

    public void saveInvoice(Invoice invoice) {
        invoiceRepository.save(invoice);
    }

    public void cancelInvoice(Long id) {
        Invoice invoice = getInvoiceById(id);
        if (invoice != null && invoice.getStatus() == InvoiceStatus.PENDING) {
            invoice.setStatus(InvoiceStatus.CANCELLED);
            invoiceRepository.save(invoice);
        } else {
            throw new IllegalStateException("Order cannot be cancelled");
        }
    }

    public void updateStatus(Long id, InvoiceStatus status) {
        Invoice invoice = getInvoiceById(id);
        if (invoice != null) {
            invoice.setStatus(status);
            invoiceRepository.save(invoice);
        }
    }
}
