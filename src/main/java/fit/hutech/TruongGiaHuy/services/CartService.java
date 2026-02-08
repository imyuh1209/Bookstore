package fit.hutech.TruongGiaHuy.services;

import fit.hutech.TruongGiaHuy.daos.Cart;
import fit.hutech.TruongGiaHuy.daos.Item;
import fit.hutech.TruongGiaHuy.entities.Book;
import fit.hutech.TruongGiaHuy.entities.Invoice;
import fit.hutech.TruongGiaHuy.entities.ItemInvoice;
import fit.hutech.TruongGiaHuy.entities.User;
import fit.hutech.TruongGiaHuy.repositories.IBookRepository;
import fit.hutech.TruongGiaHuy.repositories.IInvoiceRepository;
import fit.hutech.TruongGiaHuy.repositories.IItemInvoiceRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private static final String CART_SESSION_KEY = "CART";
    private final IInvoiceRepository invoiceRepository;
    private final IItemInvoiceRepository itemInvoiceRepository;
    private final IBookRepository bookRepository;

    public Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute(CART_SESSION_KEY);
        if (cart == null) {
            cart = new Cart();
            session.setAttribute(CART_SESSION_KEY, cart);
        }
        return cart;
    }

    public void addToCart(HttpSession session, Long id, int quantity) {
        Cart cart = getCart(session);
        Book book = bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Book not found"));
        Item item = new Item(book.getId(), book.getTitle(), book.getPrice(), quantity);
        cart.addItems(item);
    }

    public void removeFromCart(HttpSession session, Long id) {
        Cart cart = getCart(session);
        cart.removeItems(id);
    }

    public void updateCart(HttpSession session, Long id, int quantity) {
        Cart cart = getCart(session);
        cart.updateItems(id, quantity);
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    public void saveCart(HttpSession session, User user) {
        Cart cart = getCart(session);
        if (cart.getItems().isEmpty()) return;

        Invoice invoice = new Invoice();
        invoice.setInvoiceDate(new Date());
        invoice.setTotalPrice(cart.getTotalPrice());
        invoice.setUser(user);
        invoiceRepository.save(invoice);

        for (Item item : cart.getItems()) {
            Book book = bookRepository.findById(item.getBookId()).orElseThrow();
            if (book.getQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException("Not enough stock for book: " + book.getTitle());
            }
            book.setQuantity(book.getQuantity() - item.getQuantity());
            bookRepository.save(book);

            ItemInvoice itemInvoice = new ItemInvoice();
            itemInvoice.setInvoice(invoice);
            itemInvoice.setQuantity(item.getQuantity());
            itemInvoice.setPrice(item.getPrice());
            itemInvoice.setBook(book);
            itemInvoiceRepository.save(itemInvoice);
        }

        clearCart(session);
    }

    public int getSumQuantity(HttpSession session) {
        return getCart(session).getItems().stream().mapToInt(Item::getQuantity).sum();
    }

    public double getSumPrice(HttpSession session) {
        return getCart(session).getTotalPrice();
    }
}


