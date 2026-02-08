package fit.hutech.TruongGiaHuy.controllers;

import fit.hutech.TruongGiaHuy.services.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/books")
@RequiredArgsConstructor
public class AdminBookController {

    private final BookService bookService;

    @GetMapping
    public String listBooks() {
        return "redirect:/admin/books.html";
    }

    @GetMapping("/add")
    public String addBook() {
        return "redirect:/admin/book-form.html";
    }

    @GetMapping("/edit/{id}")
    public String editBook(@PathVariable Long id) {
        return "redirect:/admin/book-form.html?id=" + id;
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id) {
        bookService.deleteBookById(id);
        return "redirect:/admin/books";
    }
}


