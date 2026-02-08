package fit.hutech.TruongGiaHuy.entities;

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
@Table(name = "permission")
public class Permission implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(length = 250)
    private String description;

    @ManyToMany(mappedBy = "permissions")
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private List<Role> roles;

    @Override
    public String getAuthority() {
        return name;
    }
}


