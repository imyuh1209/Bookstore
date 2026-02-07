package fit.hutech.Huy.viewmodels;

import fit.hutech.Huy.entities.Category;

public record CategoryGetVm(Long id, String name) {
    public static CategoryGetVm from(Category category) {
        return new CategoryGetVm(category.getId(), category.getName());
    }
}
