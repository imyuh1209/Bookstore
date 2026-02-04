package fit.hutech.Huy.services;

import fit.hutech.Huy.entities.Book;
import fit.hutech.Huy.repositories.IBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private IBookRepository bookRepository;

    public List<Book> getAllBooks(Integer pageNo, Integer pageSize, String sortBy) {
        return bookRepository.findAllBooks(pageNo, pageSize, sortBy);
    }
    
    // Support returning Page object for pagination in view
    public Page<Book> getAllBooksPage(Integer pageNo, Integer pageSize, String sortBy) {
        return bookRepository.findAll(PageRequest.of(pageNo, pageSize, Sort.by(sortBy)));
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public void addBook(Book book) {
        bookRepository.save(book);
    }

    public void updateBook(Book book) {
        if (book.getId() != null && bookRepository.existsById(book.getId())) {
            bookRepository.save(book);
        }
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> searchBook(String keyword) {
        return bookRepository.searchBook(keyword);
    }
}
