package fit.hutech.TruongGiaHuy.daos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class Cart {
    private List<Item> items = new ArrayList<>();

    public void addItems(Item item) {
        boolean isExist = items.stream()
                .anyMatch(i -> Objects.equals(i.getBookId(), item.getBookId()));
        if (!isExist) {
            items.add(item);
        } else {
            items.forEach(i -> {
                if (Objects.equals(i.getBookId(), item.getBookId())) {
                    i.setQuantity(i.getQuantity() + item.getQuantity());
                }
            });
        }
    }

    public void removeItems(Long bookId) {
        items.removeIf(item -> Objects.equals(item.getBookId(), bookId));
    }

    public void updateItems(Long bookId, int quantity) {
        items.forEach(item -> {
            if (Objects.equals(item.getBookId(), bookId)) {
                item.setQuantity(quantity);
            }
        });
    }

    public double getTotalPrice() {
        return items.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
    }
}


