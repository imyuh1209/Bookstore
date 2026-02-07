package fit.hutech.Huy.controllers.api;

import fit.hutech.Huy.entities.Book;
import fit.hutech.Huy.entities.Category;
import fit.hutech.Huy.services.BookService;
import fit.hutech.Huy.services.CategoryService;
import fit.hutech.Huy.services.StorageService;
import fit.hutech.Huy.viewmodels.BookGetVm;
import fit.hutech.Huy.viewmodels.CategoryGetVm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class BookAdminApiController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final StorageService storageService;

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryGetVm>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories().stream()
                .map(CategoryGetVm::from)
                .collect(Collectors.toList()));
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<BookGetVm> getBook(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(BookGetVm::from)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/books")
    public ResponseEntity<Void> createBook(
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("price") Double price,
            @RequestParam(value = "quantity", defaultValue = "0") Integer quantity,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPrice(price);
        book.setQuantity(quantity);
        book.setDescription(description);

        Category category = categoryService.getCategoryById(categoryId);
        book.setCategory(category);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = storageService.store(imageFile, "Books");
            book.setImageUrl(imageUrl);
        }

        bookService.addBook(book);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Void> updateBook(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("author") String author,
            @RequestParam("price") Double price,
            @RequestParam(value = "quantity", defaultValue = "0") Integer quantity,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {

        Book book = bookService.getBookById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid book Id:" + id));

        book.setTitle(title);
        book.setAuthor(author);
        book.setPrice(price);
        book.setQuantity(quantity);
        book.setDescription(description);

        Category category = categoryService.getCategoryById(categoryId);
        book.setCategory(category);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = storageService.store(imageFile, "Books");
            book.setImageUrl(imageUrl);
        }

        bookService.updateBook(book);
        return ResponseEntity.ok().build();
    }
}
