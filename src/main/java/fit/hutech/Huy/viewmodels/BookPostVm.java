package fit.hutech.Huy.viewmodels;

import jakarta.validation.constraints.NotNull;

public record BookPostVm(String title, String author, Double price, Integer quantity, Long categoryId) {
}
