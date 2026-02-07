package fit.hutech.Huy.services;

import fit.hutech.Huy.daos.Cart;
import fit.hutech.Huy.daos.Item;
import fit.hutech.Huy.entities.Book;
import fit.hutech.Huy.entities.Invoice;
import fit.hutech.Huy.entities.ItemInvoice;
import fit.hutech.Huy.entities.User;
import fit.hutech.Huy.repositories.IBookRepository;
import fit.hutech.Huy.repositories.IInvoiceRepository;
import fit.hutech.Huy.repositories.IItemInvoiceRepository;
import fit.hutech.Huy.repositories.IUserRepository;
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
    private final IUserRepository userRepository;

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

    public void saveCart(HttpSession session, String username) {
        Cart cart = getCart(session);
        if (cart.getItems().isEmpty()) return;

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

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
