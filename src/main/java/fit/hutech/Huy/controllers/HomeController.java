package fit.hutech.Huy.controllers;

import fit.hutech.Huy.services.BannerService;
import fit.hutech.Huy.services.BookService;
import fit.hutech.Huy.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {
    private final BannerService bannerService;
    private final BookService bookService;
    private final CategoryService categoryService;

    @GetMapping
    public String home(Model model) {
        model.addAttribute("banners", bannerService.getActiveBanners());
        // Get top 8 books for display
        model.addAttribute("books", bookService.getAllBooksPage(0, 8, "id").getContent());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "index";
    }
}
