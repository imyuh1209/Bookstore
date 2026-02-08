package fit.hutech.TruongGiaHuy.controllers;

import fit.hutech.TruongGiaHuy.entities.Book;
import fit.hutech.TruongGiaHuy.services.BannerService;
import fit.hutech.TruongGiaHuy.services.BookService;
import fit.hutech.TruongGiaHuy.services.CategoryService;
import fit.hutech.TruongGiaHuy.services.StorageService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final CategoryService categoryService;
    private final BannerService bannerService;
    private final StorageService storageService;

    @GetMapping
    public String showAllBooks(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            Model model) {
        Page<Book> page = bookService.getAllBooksPage(pageNo, pageSize, sortBy);
        model.addAttribute("books", page.getContent());
        model.addAttribute("banners", bannerService.getActiveBanners());
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("sortBy", sortBy);
        return "book/list";
    }

    @GetMapping("/search")
    public String searchBook(@RequestParam("keyword") String keyword, Model model) {
        List<Book> books = bookService.searchBook(keyword);
        model.addAttribute("books", books);
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);
        model.addAttribute("totalItems", books.size());
        model.addAttribute("sortBy", "id");
        return "book/list";
    }

    @GetMapping("/{id}")
    public String getBookDetail(@PathVariable Long id, Model model) {
        Optional<Book> book = bookService.getBookById(id);
        if (book.isPresent()) {
            model.addAttribute("book", book.get());
            return "book/detail";
        }
        return "redirect:/books";
    }

    @GetMapping("/add")
    public String addBookForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "book/add";
    }

    @PostMapping("/add")
    public String addBook(@Valid @ModelAttribute("book") Book book, BindingResult result, 
                          @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "book/add";
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = storageService.store(imageFile, "Books");
            book.setImageUrl(imageUrl);
        }
        bookService.addBook(book);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String editBookForm(@PathVariable("id") Long id, Model model) {
        Optional<Book> book = bookService.getBookById(id);
        if (book.isPresent()) {
            model.addAttribute("book", book.get());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "book/edit";
        }
        return "redirect:/books";
    }

    @PostMapping("/edit")
    public String editBook(@Valid @ModelAttribute("book") Book book, BindingResult result,
                           @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                           Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "book/edit";
        }
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = storageService.store(imageFile, "Books");
            book.setImageUrl(imageUrl);
        } else {
            // Keep existing image if no new file is uploaded
            // Note: This requires the hidden input field for imageUrl in the edit form to work correctly
            // If the form submits the old imageUrl, we are fine.
            // If not, we might overwrite it with null if we rely on @ModelAttribute binding alone without hidden input.
            // But usually @ModelAttribute binds what's in the form. If hidden input is there, it's bound.
        }
        bookService.updateBook(book);
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        bookService.deleteBookById(id);
        return "redirect:/books";
    }
}


