package fit.hutech.TruongGiaHuy.viewmodels;

import jakarta.validation.constraints.NotNull;

public record BookPostVm(String title, String author, Double price, Integer quantity, Long categoryId, String imageUrl, String description) {
}


