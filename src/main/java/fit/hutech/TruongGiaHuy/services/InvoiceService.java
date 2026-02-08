package fit.hutech.TruongGiaHuy.services;

import fit.hutech.TruongGiaHuy.constants.InvoiceStatus;
import fit.hutech.TruongGiaHuy.entities.Book;
import fit.hutech.TruongGiaHuy.entities.Invoice;
import fit.hutech.TruongGiaHuy.entities.ItemInvoice;
import fit.hutech.TruongGiaHuy.repositories.IBookRepository;
import fit.hutech.TruongGiaHuy.repositories.IInvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService {
    private final IInvoiceRepository invoiceRepository;
    private final IBookRepository bookRepository;

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
        if (invoice != null && (invoice.getStatus() == null || invoice.getStatus() == InvoiceStatus.PENDING)) {
            invoice.setStatus(InvoiceStatus.CANCELLED);
            invoiceRepository.save(invoice);
            restoreStock(invoice);
        } else {
            throw new IllegalStateException("Order cannot be cancelled");
        }
    }

    public void updateStatus(Long id, InvoiceStatus status) {
        Invoice invoice = getInvoiceById(id);
        if (invoice != null) {
            // If order is already cancelled, prevent status update
            if (invoice.getStatus() == InvoiceStatus.CANCELLED) {
                throw new IllegalStateException("Cannot update status of a cancelled order");
            }

            // Check if transitioning to CANCELLED from a non-CANCELLED state
            // to restore stock
            if (status == InvoiceStatus.CANCELLED && invoice.getStatus() != InvoiceStatus.CANCELLED) {
                restoreStock(invoice);
            }
            
            invoice.setStatus(status);
            invoiceRepository.save(invoice);
        }
    }

    public void deleteInvoice(Long id) {
        Invoice invoice = getInvoiceById(id);
        if (invoice != null) {
            // Restore stock if order is PENDING or NULL
            if (invoice.getStatus() == null || invoice.getStatus() == InvoiceStatus.PENDING) {
                restoreStock(invoice);
            }
            invoiceRepository.delete(invoice);
        }
    }

    private void restoreStock(Invoice invoice) {
        for (ItemInvoice item : invoice.getItemInvoices()) {
            Book book = item.getBook();
            if (book != null) {
                book.setQuantity(book.getQuantity() + item.getQuantity());
                bookRepository.save(book);
            }
        }
    }
}


