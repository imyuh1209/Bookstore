package fit.hutech.Huy.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
    ADMIN(1),
    USER(2);

    public final long value;
}
