package fit.hutech.TruongGiaHuy.repositories;

import fit.hutech.TruongGiaHuy.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleById(Long id);
    Optional<Role> findRoleByName(String name);
}


