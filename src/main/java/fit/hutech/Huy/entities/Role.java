package fit.hutech.Huy.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 250)
    private String description;

    @ManyToMany(mappedBy = "roles")
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private List<User> users;

    @Override
    public String getAuthority() {
        return name;
    }
}
