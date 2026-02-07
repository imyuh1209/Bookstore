package fit.hutech.Huy.controllers;

import fit.hutech.Huy.entities.Book;
import fit.hutech.Huy.entities.Category;
import fit.hutech.Huy.services.BookService;
import fit.hutech.Huy.services.CategoryService;
import fit.hutech.Huy.viewmodels.BookGetVm;
import fit.hutech.Huy.viewmodels.BookPostVm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/books")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ApiController {

    private final BookService bookService;
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<BookGetVm>> getAllBooks() {
        List<Book> books = bookService.getAllBooks(0, Integer.MAX_VALUE, "id");
        List<BookGetVm> bookVms = books.stream()
                .map(BookGetVm::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookVms);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookGetVm> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(BookGetVm::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody @jakarta.validation.Valid BookPostVm bookPostVm, org.springframework.validation.BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        Category category = categoryService.getCategoryById(bookPostVm.categoryId());
        if (category == null) {
             return ResponseEntity.badRequest().body("Category not found");
        }
        Book book = new Book();
        book.setTitle(bookPostVm.title());
        book.setAuthor(bookPostVm.author());
        book.setPrice(bookPostVm.price());
        book.setQuantity(bookPostVm.quantity());
        book.setCategory(category);
        book.setImageUrl(bookPostVm.imageUrl());
        book.setDescription(bookPostVm.description());
        
        bookService.addBook(book);
        return ResponseEntity.ok(BookGetVm.from(book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody @jakarta.validation.Valid BookPostVm bookPostVm, org.springframework.validation.BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        
        Book existingBook = bookService.getBookById(id).orElse(null);
        if (existingBook == null) {
            return ResponseEntity.notFound().build();
        }

        Category category = categoryService.getCategoryById(bookPostVm.categoryId());
        if (category == null) {
             return ResponseEntity.badRequest().body("Category not found");
        }
        
        existingBook.setTitle(bookPostVm.title());
        existingBook.setAuthor(bookPostVm.author());
        existingBook.setPrice(bookPostVm.price());
        existingBook.setQuantity(bookPostVm.quantity());
        existingBook.setCategory(category);
        existingBook.setImageUrl(bookPostVm.imageUrl());
        existingBook.setDescription(bookPostVm.description());
        
        bookService.updateBook(existingBook);
        return ResponseEntity.ok(BookGetVm.from(existingBook));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookById(@PathVariable Long id) {
        if (bookService.getBookById(id).isPresent()) {
            bookService.deleteBookById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<BookGetVm>> searchBooks(@RequestParam String keyword) {
        List<Book> books = bookService.searchBook(keyword);
        List<BookGetVm> bookVms = books.stream()
                .map(BookGetVm::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(bookVms);
    }
}
