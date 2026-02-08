package fit.hutech.TruongGiaHuy.viewmodels;

import fit.hutech.TruongGiaHuy.entities.Book;
import jakarta.validation.constraints.NotNull;

public record BookGetVm(Long id, String title, String author, Double price, Integer quantity, String category, Long categoryId, String imageUrl, String description) {
    public static BookGetVm from(Book book) {
        String categoryName = book.getCategory() != null ? book.getCategory().getName() : "Unknown";
        Long categoryId = book.getCategory() != null ? book.getCategory().getId() : null;
        return new BookGetVm(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPrice(),
                book.getQuantity() != null ? book.getQuantity() : 0,
                categoryName,
                categoryId,
                book.getImageUrl(),
                book.getDescription()
        );
    }
}


